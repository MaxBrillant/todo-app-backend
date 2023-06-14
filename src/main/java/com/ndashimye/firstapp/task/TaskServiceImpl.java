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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final UserService userService;
    private final TodoService todoService;
    private final TaskDTOMapper taskDTOMapper;
    private TaskRepository taskRepository;



    //Service methods that handle all the operations related to tasks

    @Override
    public TaskDTO getTaskDTOById(Long taskId) throws AppEntityNotFoundException {
        log.info("Fetching task by ID: {}...", taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppEntityNotFoundException(Task.class));
        log.info("Task of ID: {} was successfully fetched.", taskId);

        return taskDTOMapper.apply(task);
    }

    @Override
    public Task getTaskById(Long taskId) throws AppEntityNotFoundException {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new AppEntityNotFoundException(Task.class));
    }

    @Override
    public List<TaskDTO> getCompletedTasks(Long todoId)
            throws AppEntityNotFoundException {

        Todo todo = todoService.getTodoById(todoId);
        log.info("Fetching all completed tasks of todo of ID: {}...", todoId);
        List<Task> tasks = taskRepository.findByCompletedTasks(todo);
        log.info("All completed tasks of todo of ID: {} were successfully fetched.", todoId);

        return tasks.stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getUncompletedTasks(Long todoId)
            throws AppEntityNotFoundException {

        Todo todo = todoService.getTodoById(todoId);
        log.info("Fetching all uncompleted tasks of todo of ID: {}...", todoId);
        List<Task> tasks = taskRepository.findByUncompletedTasks(todo);
        log.info("All uncompleted tasks of todo of ID: {} were successfully fetched.", todoId);

        return tasks.stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public void updateTask(TaskCreationDTO updatedTask, Long taskId)
            throws AppEntityNotFoundException {

        Task task = getTaskById(taskId);
        log.info("Updating task of ID: {}...", task.getTaskId());

        task.setName(updatedTask.name());
        task.setDueTime(updatedTask.dueTime());
        task.setPriorityLevel(updatedTask.priorityLevel());

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
    public void completeTask(Long userId, Long taskId)
            throws AppEntityNotFoundException {

        Task task = getTaskById(taskId);
        User user = userService.getUserById(userId);

        log.info("Completing the task...");
        task.setCompletedByUser(user);
        task.setCompletionTime(ZonedDateTime.of(LocalDateTime.now()
                , ZoneId.of(user.getSettings().getTimeZone())));
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
    public void assignPositionToNewTask(Task task)
            throws AppEntityNotFoundException {

        if (Objects.isNull(task.getParentTask())) {
            log.info("Calculating the maximum position value...");
            Optional<Task> lastTask = taskRepository.findLastTaskByTodoAndNoParentTaskAndOrderByPosition
                    (task.getTodo());
            log.info("Assigning a position to task of ID: {}...", task.getTaskId());
            if (lastTask.isEmpty()) {
                task.setPosition(1);
            } else {
                task.setPosition(lastTask.get().getPosition() + 1);
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
    public void addNewTaskToTodo(TaskCreationDTO taskCreationDTO, Long todoId)
            throws AppEntityNotFoundException {

        Todo todo = todoService.getTodoById(todoId);
        log.info("Adding a new task to todo of ID: {}...", todo.getTodoId());

        Task task = Task.builder()
                .todo(todo)
                .parentTask(taskCreationDTO.parentTaskId()==null?null:getTaskById(taskCreationDTO.parentTaskId()))
                .name(taskCreationDTO.name())
                .dueTime(taskCreationDTO.dueTime())
                .priorityLevel(taskCreationDTO.priorityLevel())
                .build();

        assignPositionToNewTask(task);

        taskRepository.save(task);

        log.info("Task of ID: {} was successfully added to todo of ID: {}."
                , task.getTaskId(), todo.getTodoId());
    }

    @Override
    public List<TaskDTO> getAllTasksByTodoId(Long todoId) throws AppEntityNotFoundException {

        Todo todo = todoService.getTodoById(todoId);
        log.info("Fetching all tasks of todo of ID: {}...", todoId);
        List<Task> tasks = taskRepository.findByTodoAndOrderByPositionAsc(todo);
        log.info("All tasks of todo of ID: {} were successfully fetched.", todoId);

        return tasks.stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getLastTasksByTodoId(Long todoId) throws AppEntityNotFoundException {

        Todo todo = todoService.getTodoById(todoId);
        log.info("Fetching the last tasks of todo of ID: {}...", todoId);
        List<Task> tasks = taskRepository.findByTodoAndOrderByPositionDesc(todo);
        log.info("The last tasks of todo of ID: {} were successfully fetched.", todoId);

        return tasks.stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getAllTasksByTodoIdOrderedByPriority(Long todoId)
            throws AppEntityNotFoundException {

        Todo todo = todoService.getTodoById(todoId);
        log.info("Fetching all tasks of todo of ID: {} ordered by priority...", todoId);
        List<Task> tasks = taskRepository.findByTodoAndOrderByPriorityLevelDesc(todo);
        log.info("All tasks of todo of ID: {} ordered by priority were successfully fetched.", todoId);

        return tasks.stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());
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
    public List<TaskDTO> getAllChildTasksByTaskId(Long taskId)
            throws AppEntityNotFoundException {

        Task task = getTaskById(taskId);
        log.info("Fetching all child tasks of task of ID: {}...", taskId);
        List<Task> tasks = taskRepository.findByParentTaskAndOrderByPositionAsc(task);
        log.info("All child tasks of task of ID: {} were successfully fetched.", taskId);

        return tasks.stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getLastChildTasksByTaskId(Long taskId)
            throws AppEntityNotFoundException {

        Task task = getTaskById(taskId);
        log.info("Fetching the last child tasks of task of ID: {}...", taskId);
        List<Task> tasks = taskRepository.findByParentTaskAndOrderByPositionDesc(task);
        log.info("The last child tasks of task of ID: {} were successfully fetched.", taskId);

        return tasks.stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getAllChildTasksByTaskIdOrderedByPriority(Long taskId)
            throws AppEntityNotFoundException {

        Task task = getTaskById(taskId);
        log.info("Fetching all child tasks of task of ID: {} ordered by priority...", taskId);
        List<Task> tasks = taskRepository.findByParentTaskAndOrderByPriorityLevelDesc(task);
        log.info("All child tasks of task of ID: {} ordered by priority were successfully fetched.", taskId);

        return tasks.stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getCompletedChildTasks(Long taskId)
            throws AppEntityNotFoundException {

        Task task = getTaskById(taskId);
        log.info("Fetching all completed child tasks of task of ID: {}...", taskId);
        List<Task> tasks = taskRepository.findByCompletedChildTasks(task);
        log.info("All completed child tasks of task of ID: {} were successfully fetched.", taskId);

        return tasks.stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getUncompletedChildTasks(Long taskId)
            throws AppEntityNotFoundException {

        Task task = getTaskById(taskId);
        log.info("Fetching all uncompleted child tasks of task of ID: {}...", taskId);
        List<Task> tasks = taskRepository.findByUncompletedChildTasks(task);
        log.info("All uncompleted child tasks of task of ID: {} were successfully fetched.", taskId);

        return tasks.stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }
}
