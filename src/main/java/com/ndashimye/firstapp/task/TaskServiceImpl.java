package com.ndashimye.firstapp.task;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.todo.Todo;
import com.ndashimye.firstapp.todo.TodoService;
import com.ndashimye.firstapp.user.User;
import com.ndashimye.firstapp.user.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final UserService userService;
    private final TodoService todoService;
    private TaskRepository taskRepository;



    //Service methods that handle all the operations related to tasks

    @Override
    public Task getTaskById(Long taskId) throws AppEntityNotFoundException {
        log.info("Fetching task by ID: {}...", taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppEntityNotFoundException(Task.class));
        log.info("Task of ID: {} was successfully fetched.", taskId);

        return task;
    }

    @Override
    public List<Task> getCompletedTasks(Long todoId) throws AppEntityNotFoundException {

        Todo todo = todoService.getTodoById(todoId);
        log.info("Fetching all completed tasks of todo of ID: {}...", todoId);
        List<Task> tasks = taskRepository.findByCompletedTasks(todo);
        log.info("All completed tasks of todo of ID: {} were successfully fetched.", todoId);

        return tasks;
    }

    @Override
    public List<Task> getUncompletedTasks(Long todoId) throws AppEntityNotFoundException {

        Todo todo = todoService.getTodoById(todoId);
        log.info("Fetching all uncompleted tasks of todo of ID: {}...", todoId);
        List<Task> tasks = taskRepository.findByUncompletedTasks(todo);
        log.info("All uncompleted tasks of todo of ID: {} were successfully fetched.", todoId);

        return tasks;
    }

    @Override
    public void updateTask(Task updatedTask, Long taskId) throws AppEntityNotFoundException {

        Task task = getTaskById(taskId);
        log.info("Updating task of ID: {}...", task.getTaskId());


        if (Objects.nonNull(updatedTask.getName()) && !updatedTask.getName().equals("")) {
            task.setName(updatedTask.getName());
        }
        if (Objects.nonNull(updatedTask.getDueTime()) && !updatedTask.getDueTime().equals("")) {
            task.setDueTime(updatedTask.getDueTime());
        }
        if (Objects.nonNull(updatedTask.getPriorityLevel()) && !String.valueOf(updatedTask.getPriorityLevel()).equals("")) {
            task.setPriorityLevel(updatedTask.getPriorityLevel());
        }

        taskRepository.save(task);
        log.info("Task of ID: {} was successfully updated.", task.getTaskId());
    }

    @Override
    public void deleteTask(Long taskId)
            throws AppEntityNotFoundException {

        Task task = getTaskById(taskId);
        log.info("Deleting task of ID: {}...", task.getTaskId());
        Task deletedTask = task;
        taskRepository.delete(task);
        log.info("Task of ID: {} was successfully deleted.", deletedTask.getTaskId());
    }

    @Override
    public void completeTask(Long userId, Long taskId) throws AppEntityNotFoundException {
        Task task = getTaskById(taskId);
        User user = userService.getUserById(userId);

        log.info("Completing the task...");
        task.setCompletedByUser(user);
        task.setCompletionTime(ZonedDateTime.of(LocalDateTime.now(), ZoneId.of(user.getSettings().getTimeZone())));
        log.info("Task of ID: {} was successfully completed by user of ID: {} and username: {}."
                , task.getTaskId(), user.getUserId(), user.getUsername());
    }

    @Override
    public void unCompleteTask(Long taskId) throws AppEntityNotFoundException {
        Task task = getTaskById(taskId);

        log.info("Uncompleting the task...");
        task.setCompletedByUser(null);
        task.setCompletionTime(null);
        log.info("Task of ID: {} was successfully uncompleted."
                , task.getTaskId());

    }

    @Override
    public void assignPositionToNewTask(Task task) throws AppEntityNotFoundException {

        if (Objects.isNull(task.getParentTask())) {
            log.info("Calculating the maximum position value...");
            Task lastTask = taskRepository.findLastTaskByTodoAndNoParentTaskAndOrderByPosition
                    (task.getTodo()).orElseThrow(() -> new AppEntityNotFoundException(Task.class));
            log.info("Assigning a position to task of ID: {}...", task.getTaskId());
            if (Objects.isNull(lastTask)) {
                task.setPosition(1);
            } else {
                task.setPosition(lastTask.getPosition() + 1);
            }
        } else {
            log.info("Calculating the maximum position value of all tasks " +
                            "that have as parent the task of ID: {}..."
                    , task.getParentTask().getTaskId());

            List<Task> childTasks = task.getParentTask().getChildTasks();
            log.info("Assigning a position to task of ID: {} and parent of ID: {}..."
                    , task.getTaskId(), task.getParentTask().getTaskId());

            if (childTasks.isEmpty()) {
                task.setPosition(1);
            } else {
                Task lastChildTask = childTasks.get(childTasks.size() - 1);
                task.setPosition(lastChildTask.getPosition() + 1);
            }
        }
        log.info("Position of value: {} was successfully assigned to the new task of ID: {}."
                , task.getPosition(), task.getTaskId());
    }

    @Override
    public void updateTaskPosition(Long taskId, int newPosition)
            throws AppEntityNotFoundException {

        Task task = getTaskById(taskId);
        Todo todo = task.getTodo();

        log.info("Getting the current position of task of ID: {}...", task.getTaskId());
        int currentPosition = task.getPosition();

        if (newPosition > currentPosition) {
            List<Task> tasksToUpdate = taskRepository
                    .findByTodoAndPositionBetweenOrderByPositionAsc
                            (todo, task.getParentTask(), currentPosition + 1, newPosition);
            log.info("Updating positions of tasks that are between position {} and {}", currentPosition, newPosition);
            for (Task taskToUpdate : tasksToUpdate) {
                taskToUpdate.setPosition(taskToUpdate.getPosition() - 1);
            }
            log.info("The positions of all tasks that are between position {} and {} were successfully updated."
                    , currentPosition, newPosition);

        } else if (newPosition < currentPosition) {
            List<Task> tasksToUpdate = taskRepository.findByTodoAndPositionBetweenOrderByPositionAsc
                    (todo, task.getParentTask(), newPosition, currentPosition - 1);
            log.info("Updating positions of tasks that are between position {} and {}", newPosition, currentPosition);
            for (Task taskToUpdate : tasksToUpdate) {
                taskToUpdate.setPosition(taskToUpdate.getPosition() + 1);
            }
            log.info("The positions of all tasks that are between position {} and {} were successfully updated."
                    , newPosition, currentPosition);
        }

        log.info("Updating the position of task of ID: {}...", task.getTaskId());
        task.setPosition(newPosition);
        log.info("The position of task of ID: {} was successfully updated.", task.getTaskId());
    }


    /*

    Service methods that handle all the operations
    related to the relationship between tasks and todos

    */

    @Override
    public void addNewTaskToTodo(Task task, Long todoId)
            throws AppEntityNotFoundException {

        Todo todo = todoService.getTodoById(todoId);
        log.info("Adding a new task to todo of ID: {}...", todo.getTodoId());
        task.setTodo(todo);
        assignPositionToNewTask(task);
        taskRepository.save(task);
        log.info("Task of ID: {} was successfully added to todo of ID: {}."
                , task.getTaskId(), todo.getTodoId());
    }

    @Override
    public List<Task> getAllTasksByTodoId(Long todoId) throws AppEntityNotFoundException {

        Todo todo = todoService.getTodoById(todoId);
        log.info("Fetching all tasks of todo of ID: {}...", todoId);
        List<Task> tasks = taskRepository.findByTodoAndOrderByPositionAsc(todo);
        log.info("All tasks of todo of ID: {} were successfully fetched.", todoId);

        return tasks;
    }

    @Override
    public List<Task> getAllTasksByTodoIdOrderedByPriority(Long todoId) throws AppEntityNotFoundException {

        Todo todo = todoService.getTodoById(todoId);
        log.info("Fetching all tasks of todo of ID: {} ordered by priority...", todoId);
        List<Task> tasks = taskRepository.findByTodoAndOrderByPriorityLevelDesc(todo);
        log.info("All tasks of todo of ID: {} ordered by priority were successfully fetched.", todoId);

        return tasks;
    }



    /*

    Service methods that handle all the operations
    related to the relationship between tasks and their parent tasks

    */

    @Override
    public void updateParentTask(Long taskId, Long parentTaskId, int position)
            throws AppEntityNotFoundException {

        Task task = getTaskById(taskId);
        Task parentTask = getTaskById(parentTaskId);

        if(parentTask.getTodo().equals(task.getTodo())) {
            if (!parentTask.equals(task)) {

                log.info("Assigning a parent task of ID: {} to task of ID: {}..."
                        , parentTask.getTaskId(), task.getTaskId());

                log.info("Repositioning child tasks that belong to the old parent task...");
                if (Objects.isNull(task.getParentTask())) {
                    //get the very last task ranked by position
                    Task lastTask = taskRepository.findLastTaskByTodoAndNoParentTaskAndOrderByPosition
                            (task.getTodo()).orElseThrow(() -> new AppEntityNotFoundException(Task.class));
                    //checking if the task is not the only one contained within its parent task.
                    if(lastTask.getPosition() > 1) {
                        //Give the task the very last position within its parent task.
                        updateTaskPosition(taskId, lastTask.getPosition() + 1);
                    }
                }else {
                    List<Task> childTasks = task.getParentTask().getChildTasks();
                    if (childTasks.size() > 1) {
                        //get the very last task ranked by position
                        Task lastChildTask = childTasks.get(childTasks.size() - 1);
                        updateTaskPosition(taskId, lastChildTask.getPosition() + 1);
                    }
                }


                task.setParentTask(parentTask);
                taskRepository.save(task);
                log.info("Positioning task of ID: {} into its new parent task...", taskId);
                updateTaskPosition(taskId, position);
                log.info("Parent task of ID: {} was successfully assigned to task of ID: {}."
                        , parentTask.getTaskId(), task.getTaskId());
            }else {
                log.error("ERROR: the task must be different from its parent.");
            }
        }else {
            log.error("ERROR: the parent task and the task must have the same todos.");
        }
    }



    /*

    Service methods that handle all the operations
    related to the relationship between tasks and their children tasks (sub-tasks)

    */

    @Override
    public List<Task> getAllChildTasksByTaskId(Long taskId) throws AppEntityNotFoundException {

        Task task = getTaskById(taskId);
        log.info("Fetching all child tasks of task of ID: {}...", taskId);
        List<Task> tasks = taskRepository.findByParentTaskAndOrderByPositionAsc(task);
        log.info("All child tasks of task of ID: {} were successfully fetched.", taskId);

        return tasks;
    }

    @Override
    public List<Task> getAllChildTasksByTaskIdOrderedByPriority(Long taskId) throws AppEntityNotFoundException {

        Task task = getTaskById(taskId);
        log.info("Fetching all child tasks of task of ID: {} ordered by priority...", taskId);
        List<Task> tasks = taskRepository.findByParentTaskAndOrderByPriorityLevelDesc(task);
        log.info("All child tasks of task of ID: {} ordered by priority were successfully fetched.", taskId);

        return tasks;
    }

    @Override
    public List<Task> getCompletedChildTasks(Long taskId) throws AppEntityNotFoundException {

        Task task = getTaskById(taskId);
        log.info("Fetching all completed child tasks of task of ID: {}...", taskId);
        List<Task> tasks = taskRepository.findByCompletedChildTasks(task);
        log.info("All completed child tasks of task of ID: {} were successfully fetched.", taskId);

        return tasks;
    }

    @Override
    public List<Task> getUncompletedChildTasks(Long taskId) throws AppEntityNotFoundException {

        Task task = getTaskById(taskId);
        log.info("Fetching all uncompleted child tasks of task of ID: {}...", taskId);
        List<Task> tasks = taskRepository.findByUncompletedChildTasks(task);
        log.info("All uncompleted child tasks of task of ID: {} were successfully fetched.", taskId);

        return tasks;
    }
}
