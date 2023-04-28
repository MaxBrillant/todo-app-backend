package com.ndashimye.firstapp.todo;

import com.ndashimye.firstapp.task.Task;
import com.ndashimye.firstapp.task.TaskRepository;
import com.ndashimye.firstapp.usertodo.UserTodo;
import com.ndashimye.firstapp.usertodo.UserTodoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Slf4j
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserTodoRepository userTodoRepository;

    @Autowired
    private TaskRepository taskRepository;

    public Todo getTodoById(Long todoId) throws TodoNotFoundException {
        log.info("Fetching todo by ID {}...", todoId);
        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new TodoNotFoundException());
        log.info("Todo of ID: {} was successfully fetched.", todoId);

        return todo;
    }


    public List<Task> getAllTasksByTodoId(Long todoId) throws TodoNotFoundException {

        Todo todo = getTodoById(todoId);
        log.info("Fetching all tasks of todo of ID: {}...", todoId);
        List<Task> tasks = taskRepository.findByTodoTask_TodoOrderByTodoTask_PositionAsc(todo);
        log.info("All tasks of todo of ID: {} were successfully fetched.", todoId);

        return tasks;
    }


    public List<Task> getAllTasksByTodoIdOrderedByPriority(Long todoId) throws TodoNotFoundException {

        Todo todo = getTodoById(todoId);
        log.info("Fetching all tasks of todo of ID: {} ordered by priority...", todoId);
        List<Task> tasks = taskRepository.findByTodoTask_TodoOrderByTodoTask_PriorityLevelDesc(todo);
        log.info("All tasks of todo of ID: {} ordered by priority were successfully fetched.", todoId);

        return tasks;
    }

    public List<Task> getCompletedTasks(Long todoId) throws TodoNotFoundException {

        Todo todo = getTodoById(todoId);
        log.info("Fetching all completed tasks of todo of ID: {}...", todoId);
        List<Task> tasks = taskRepository.findByCompletedTasks(todo);
        log.info("All completed tasks of todo of ID: {} were successfully fetched.", todoId);

        return tasks;
    }

    public List<Task> getUncompletedTasks(Long todoId) throws TodoNotFoundException {

        Todo todo = getTodoById(todoId);
        log.info("Fetching all uncompleted tasks of todo of ID: {}...", todoId);
        List<Task> tasks = taskRepository.findByUncompletedTasks(todo);
        log.info("All uncompleted tasks of todo of ID: {} were successfully fetched.", todoId);

        return tasks;
    }

    public void addNewTodo(Todo todo) {

        log.info("Adding a new todo...");
        todoRepository.save(todo);
        log.info("Todo of ID: {} was successfully added.", todo.getTodoId());
    }

    public void updateTodo(Todo updatedTodo, Long todoId) throws TodoNotFoundException {

        Todo todo = getTodoById(todoId);
        log.info("Updating todo of ID: {}...", todo.getTodoId());

        if (Objects.nonNull(updatedTodo.getName()) && !updatedTodo.getName().equals("")) {
            todo.setName(updatedTodo.getName());
        }
        if (Objects.nonNull(updatedTodo.getDescription()) && !updatedTodo.getDescription().equals("")) {
            todo.setDescription(updatedTodo.getDescription());
        }
        if (Objects.nonNull(updatedTodo.getDueTime()) && !updatedTodo.getDueTime().equals("")) {
            todo.setDueTime(updatedTodo.getDueTime());
        }
        if (Objects.nonNull(updatedTodo.getIsRecurrent()) && !String.valueOf(updatedTodo.getIsRecurrent()).equals("")) {
            todo.setIsRecurrent(updatedTodo.getIsRecurrent());
        }

        todoRepository.save(todo);
        log.info("Todo of ID: {} was successfully updated.", todo.getTodoId());
    }

    public void deleteTodo(Long todoId) throws TodoNotFoundException {

        Todo todo = getTodoById(todoId);
        log.info("Deleting todo of ID: {}...", todo.getTodoId());
        Todo deletedTodo = todo;
        todoRepository.delete(todo);
        log.info("Todo of ID: {} was successfully deleted.", deletedTodo.getTodoId());
    }



    public void addNewUserTodo(Long todoId, UserTodo userTodo) throws TodoNotFoundException {

        Todo todo = getTodoById(todoId);
        log.info("Assigning todo of ID: {} to user of ID: {} and username: {}..."
                , todo.getTodoId(), userTodo.getUser().getUserId(), userTodo.getUser().getUsername());

        if (Objects.isNull(todo.getUserTodo())) {
            userTodoRepository.save(userTodo);
            todo.setUserTodo(userTodo);
            assignPositionToNewTodo(todo);
            log.info("Todo of ID: {} was successfully assigned to user of ID: {} and username: {}."
                    , todo.getTodoId(), userTodo.getUser().getUserId(), userTodo.getUser().getUsername());
        }else {
            log.error("ERROR: Todo of ID: {} has already been assigned to another user, try to update it instead."
                    , todo.getTodoId());
        }
    }


    public void assignPositionToNewTodo(Todo todo) {

        log.info("Calculating the maximum position value of all existing todos...");
        // Get the maximum position value from all existing todos
        Integer maxPosition = todoRepository.getMaxPosition();

        // If there are no existing todos, set the position to 1
        if (maxPosition == null) {
            maxPosition = 0;
        }

        log.info("Assigning a position to a new todo of ID: {}...", todo.getTodoId());
        // Assign the new todo's position to be the maximum position + 1
        todo.getUserTodo().setPosition(maxPosition + 1);

        // Save the new todo
        todoRepository.save(todo);
        log.info("Position of value: {} was successfully assigned to the new todo of ID: {}."
                , todo.getUserTodo().getPosition(), todo.getTodoId());
    }

    public void updateUserTodo(Long todoId, UserTodo updatedUserTodo) throws TodoNotFoundException {

        Todo todo = getTodoById(todoId);
        log.info("Updating information related to the assignment of todo of ID: {} to a user..."
                , todo.getTodoId());

        if (Objects.nonNull(todo.getUserTodo())) {

            if (Objects.nonNull(updatedUserTodo.getUser()) && !updatedUserTodo.getUser().equals("")) {
                todo.getUserTodo().setUser(updatedUserTodo.getUser());
            }
            if (Objects.nonNull(updatedUserTodo.getPriorityLevel()) && !String.valueOf(updatedUserTodo.getPriorityLevel()).equals("")) {
                todo.getUserTodo().setPriorityLevel(updatedUserTodo.getPriorityLevel());
            }
            log.info("The information related to the assignment of todo of ID: {} to a user was successfully updated."
                    , todo.getTodoId());
        }else {
            log.error("Todo of ID: {} is not assigned to any user, try to assign it to a user instead."
                    , todo.getTodoId());
        }
    }


    public void updateTodoPosition(Long todoId, int newPosition) throws TodoNotFoundException {

        Todo todo = getTodoById(todoId);

        log.info("Getting the current position of todo of ID: {}...", todo.getTodoId());
        // Get the current position of the todo
        int currentPosition = todo.getUserTodo().getPosition();

        // If the new position is equal to the current position, do nothing
        if (newPosition == currentPosition) {
            return;
        }

        log.info("Getting all todos with positions between the current ({}) and new ({}) positions..."
                , currentPosition, newPosition);

        // Get the todos with positions between the current and new positions
        List<Todo> todosToUpdate;
        if (newPosition > currentPosition) {
            todosToUpdate = todoRepository.findTodosWithPositionsBetween(currentPosition + 1, newPosition);
        } else {
            todosToUpdate = todoRepository.findTodosWithPositionsBetween(newPosition, currentPosition - 1);
        }

        log.info("Updating the positions of all the todos with positions between {} and {}...", currentPosition, newPosition);
        // Update the positions of the affected todos
        for (Todo todoToUpdate : todosToUpdate) {
            UserTodo userTodo = todoToUpdate.getUserTodo();
            if (newPosition > currentPosition) {
                userTodo.setPosition(userTodo.getPosition() - 1);
            } else {
                userTodo.setPosition(userTodo.getPosition() + 1);
            }
            todoRepository.save(todoToUpdate);
        }
        log.info("The positions of all todos with positions between {} and {} were successfully updated."
                , currentPosition, newPosition);

        log.info("Updating the position of todo of ID: {}...", todo.getTodoId());
        // Update the position of the target todo
        todo.getUserTodo().setPosition(newPosition);
        todoRepository.save(todo);
        log.info("The position of todo of ID: {} was successfully updated.", todo.getTodoId());
    }

    public void deleteUserTodo(Long todoId) throws TodoNotFoundException {

        Todo todo = getTodoById(todoId);
        log.info("Deleting information related to the assignment of todo of ID: {} to a user..."
                , todo.getTodoId());

        if (Objects.nonNull(todo.getUserTodo())) {
            userTodoRepository.delete(todo.getUserTodo());
            log.info("The information related to the assignment of todo of ID: {} to a user was successfully deleted."
                    , todo.getTodoId());
        }else {
            log.error("Todo of ID: {} is not assigned to any user, try to assign it to a user instead."
                    , todo.getTodoId());
        }
    }
}
