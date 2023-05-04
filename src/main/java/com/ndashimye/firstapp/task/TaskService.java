package com.ndashimye.firstapp.task;

import com.ndashimye.firstapp.todo.Todo;
import com.ndashimye.firstapp.todo.TodoNotFoundException;
import com.ndashimye.firstapp.todotask.TodoTask;
import com.ndashimye.firstapp.todotask.TodoTaskNotFoundException;
import com.ndashimye.firstapp.todotask.TodoTaskRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TodoTaskRepository todoTaskRepository;

    public Task getTaskById(Long taskId) throws TaskNotFoundException {
        log.info("Fetching task by ID: {}...", taskId);
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException());
        log.info("Task of ID: {} was successfully fetched.", taskId);

        return task;
    }


    public List<Task> getAllChildTasksByTaskId(Long taskId) throws TaskNotFoundException {

        Task task = getTaskById(taskId);
        log.info("Fetching all child tasks of task of ID: {}...", taskId);
        List<Task> tasks = taskRepository.findByParentTaskOrderByTodoTask_PositionAsc(task);
        log.info("All child tasks of task of ID: {} were successfully fetched.", taskId);

        return tasks;
    }


    public List<Task> getAllChildTasksByTaskIdOrderedByPriority(Long taskId) throws TaskNotFoundException {

        Task task = getTaskById(taskId);
        log.info("Fetching all child tasks of task of ID: {} ordered by priority...", taskId);
        List<Task> tasks = taskRepository.findByParentTaskOrderByTodoTask_PriorityLevelDesc(task);
        log.info("All child tasks of task of ID: {} ordered by priority were successfully fetched.", taskId);

        return tasks;
    }

    public List<Task> getCompletedChildTasks(Long taskId) throws TaskNotFoundException {

        Task task = getTaskById(taskId);
        log.info("Fetching all completed child tasks of task of ID: {}...", taskId);
        List<Task> tasks = taskRepository.findByCompletedChildTasks(task);
        log.info("All completed child tasks of task of ID: {} were successfully fetched.", taskId);

        return tasks;
    }

    public List<Task> getUncompletedChildTasks(Long taskId) throws TaskNotFoundException {

        Task task = getTaskById(taskId);
        log.info("Fetching all uncompleted child tasks of task of ID: {}...", taskId);
        List<Task> tasks = taskRepository.findByUncompletedChildTasks(task);
        log.info("All uncompleted child tasks of task of ID: {} were successfully fetched.", taskId);

        return tasks;
    }



    public void addNewTask(Task task) {
        log.info("Adding a new task...");
        taskRepository.save(task);
        log.info("Task of ID: {} was successfully added.", task.getTaskId());
    }

    public void updateTask(Task updatedTask, Long taskId) throws TaskNotFoundException {

        Task task = getTaskById(taskId);
        log.info("Updating task of ID: {}...", task.getTaskId());

        if (!task.equals(updatedTask)) {
            if (Objects.nonNull(updatedTask.getParentTask())) {
                if (!updatedTask.getParentTask().equals("")) {
                    task.setParentTask(updatedTask.getParentTask());
                }
            } else {
                task.setParentTask(updatedTask.getParentTask());
            }
        }


        if (Objects.nonNull(updatedTask.getName()) && !updatedTask.getName().equals("")) {
            task.setName(updatedTask.getName());
        }

        taskRepository.save(task);
        log.info("Task of ID: {} was successfully updated.", task.getTaskId());
    }

    public void deleteTask(Long taskId) throws TaskNotFoundException {

        Task task = getTaskById(taskId);
        log.info("Deleting task of ID: {}...", task.getTaskId());
        Task deletedTask = task;
        taskRepository.delete(task);
        log.info("Task of ID: {} was successfully deleted.", deletedTask.getTaskId());
    }



    public void addNewTodoTask(Long taskId, TodoTask todoTask) throws TaskNotFoundException {

        Task task = getTaskById(taskId);
        log.info("Assigning task of ID: {} to todo of ID: {}..."
                , task.getTaskId(), todoTask.getTodo().getTodoId());

        if (Objects.isNull(task.getTodoTask())) {
            todoTaskRepository.save(todoTask);
            task.setTodoTask(todoTask);
            assignPositionToNewTask(task);
            log.info("Task of ID: {} was successfully assigned to todo of ID: {}."
                    , task.getTaskId(), todoTask.getTodo().getTodoId());
        }else {
            log.error("ERROR: Task of ID: {} has already been assigned to another todo, try to update it instead."
                    , task.getTaskId());
        }
    }

    public void assignPositionToNewTask(Task task) {

        if (Objects.isNull(task.getParentTask())) {
            log.info("Calculating the maximum position value...");
            TodoTask lastTask = todoTaskRepository.findTopByTodoOrderByPositionDesc(task.getTodoTask().getTodo());
            log.info("Assigning a position to task of ID: {}...", task.getTaskId());
            if (Objects.isNull(lastTask)) {
                task.getTodoTask().setPosition(1);
            } else {
                task.getTodoTask().setPosition(lastTask.getPosition() + 1);
            }
        } else {
            log.info("Calculating the maximum position value of all tasks that have as parent the task of ID: {}..."
                    , task.getParentTask().getTaskId());

            List<Task> childTasks = task.getParentTask().getChildTasks();
            log.info("Assigning a position to task of ID: {} and parent of ID: {}..."
                    , task.getTaskId(), task.getParentTask().getTaskId());

            if (childTasks.isEmpty()) {
                task.getTodoTask().setPosition(1);
            } else {
                Task lastChildTask = childTasks.get(childTasks.size() - 1);
                task.getTodoTask().setPosition(lastChildTask.getTodoTask().getPosition() + 1);
            }
        }
        log.info("Position of value: {} was successfully assigned to the new task of ID: {}."
                , task.getTodoTask().getPosition(), task.getTaskId());
    }


    public void updateTodoTask(Long taskId, TodoTask updatedTodoTask) throws TaskNotFoundException {

        Task task = getTaskById(taskId);
        log.info("Updating information related to the assignment of task of ID: {} to a todo..."
                , task.getTaskId());

        if (Objects.nonNull(task.getTodoTask())) {
            if (Objects.nonNull(updatedTodoTask.getTodo()) && !updatedTodoTask.getTodo().equals("")) {
                task.getTodoTask().setTodo(updatedTodoTask.getTodo());
            }
            if (Objects.nonNull(updatedTodoTask.getCompletionTime()) && !updatedTodoTask.getCompletionTime().equals("")) {
                task.getTodoTask().setCompletionTime(updatedTodoTask.getCompletionTime());
            }
            if (Objects.nonNull(updatedTodoTask.getPriorityLevel()) && !String.valueOf(updatedTodoTask.getPriorityLevel()).equals("")) {
                task.getTodoTask().setPriorityLevel(updatedTodoTask.getPriorityLevel());
            }
            if (Objects.nonNull(updatedTodoTask.getIsCompleted()) && !String.valueOf(updatedTodoTask.getIsCompleted()).equals("")) {
                task.getTodoTask().setIsCompleted(updatedTodoTask.getIsCompleted());
            }
            log.info("The information related to the assignment of task of ID: {} to todo of ID: {} was successfully updated."
                    , task.getTaskId(), task.getTodoTask().getTodo().getTodoId());
        }else {
            log.error("ERROR: Task of ID: {} is not assigned to any todo, try to assign it to a todo instead."
                    , task.getTaskId());
        }
    }

    public void updateTaskPosition(Long taskId, int newPosition) throws TaskNotFoundException, TodoTaskNotFoundException, TodoNotFoundException {

        Task task = getTaskById(taskId);
        TodoTask todoTask = Optional.of(task.getTodoTask()).orElseThrow(() -> new TodoTaskNotFoundException());
        Todo todo = Optional.of(todoTask.getTodo()).orElseThrow(() -> new TodoNotFoundException());
        // TODO: 5/2/2023 CHANGE THE GET methods for all entities.

        log.info("Getting the current position of task of ID: {}...", task.getTaskId());
        int currentPosition = todoTask.getPosition();

        if (newPosition > currentPosition) {
            List<TodoTask> tasksToUpdate = todoTaskRepository.findByTodoAndPositionBetweenOrderByPositionAsc(todo, currentPosition + 1, newPosition);
            log.info("Updating positions of tasks that are between position {} and {}", currentPosition, newPosition);
            for (TodoTask taskToUpdate : tasksToUpdate) {
                taskToUpdate.setPosition(taskToUpdate.getPosition() - 1);
            }
            log.info("The positions of all tasks that are between position {} and {} were successfully updated."
                    , currentPosition, newPosition);
    // TODO: 5/2/2023 REMEMBER TO CHANGE THE POSITION OF A TASK EACH TIME IT IS ASSIGNED A NEW PARENT TASK
        } else if (newPosition < currentPosition) {
            List<TodoTask> tasksToUpdate = todoTaskRepository.findByTodoAndPositionBetweenOrderByPositionDesc(todo, newPosition, currentPosition - 1);
            log.info("Updating positions of tasks that are between position {} and {}", newPosition, currentPosition);
            for (TodoTask taskToUpdate : tasksToUpdate) {
                taskToUpdate.setPosition(taskToUpdate.getPosition() + 1);
            }
            log.info("The positions of all tasks that are between position {} and {} were successfully updated."
                    , newPosition, currentPosition);
        }

        log.info("Updating the position of task of ID: {}...", task.getTaskId());
        todoTask.setPosition(newPosition);
        log.info("The position of task of ID: {} was successfully updated.", task.getTaskId());
    }


    public void deleteTodoTask(Long taskId) throws TaskNotFoundException {

        Task task = getTaskById(taskId);
        log.info("Deleting information related to the assignment of task of ID: {} to a todo..."
                , task.getTaskId());

        if (Objects.nonNull(task.getTodoTask())) {
            todoTaskRepository.delete(task.getTodoTask());
            log.info("The information related to the assignment of task of ID: {} to a todo was successfully deleted."
                    , task.getTaskId());
        }else {
            log.error("ERROR: Task of ID: {} is not assigned to any todo, try to assign it to a todo instead."
                    , task.getTaskId());
        }
    }
}
