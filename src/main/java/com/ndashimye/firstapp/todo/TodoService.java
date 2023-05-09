package com.ndashimye.firstapp.todo;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.task.Task;
import com.ndashimye.firstapp.task.TaskRepository;
import com.ndashimye.firstapp.user.User;
import com.ndashimye.firstapp.user.UserService;
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
    private UserService userService;

    @Autowired
    private UserTodoRepository userTodoRepository;

    @Autowired
    private TaskRepository taskRepository;

    public Todo getTodoById(Long todoId) throws AppEntityNotFoundException {
        log.info("Fetching todo by ID: {}...", todoId);
        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new AppEntityNotFoundException(Todo.class));
        log.info("Todo of ID: {} was successfully fetched.", todoId);

        return todo;
    }


    public List<Task> getAllTasksByTodoId(Long todoId) throws AppEntityNotFoundException {

        Todo todo = getTodoById(todoId);
        log.info("Fetching all tasks of todo of ID: {}...", todoId);
        List<Task> tasks = taskRepository.findByTodoTask_TodoOrderByTodoTask_PositionAsc(todo);
        log.info("All tasks of todo of ID: {} were successfully fetched.", todoId);

        return tasks;
    }


    public List<Task> getAllTasksByTodoIdOrderedByPriority(Long todoId) throws AppEntityNotFoundException {

        Todo todo = getTodoById(todoId);
        log.info("Fetching all tasks of todo of ID: {} ordered by priority...", todoId);
        List<Task> tasks = taskRepository.findByTodoTask_TodoOrderByTodoTask_PriorityLevelDesc(todo);
        log.info("All tasks of todo of ID: {} ordered by priority were successfully fetched.", todoId);

        return tasks;
    }

    public List<Task> getCompletedTasks(Long todoId) throws AppEntityNotFoundException {

        Todo todo = getTodoById(todoId);
        log.info("Fetching all completed tasks of todo of ID: {}...", todoId);
        List<Task> tasks = taskRepository.findByCompletedTasks(todo);
        log.info("All completed tasks of todo of ID: {} were successfully fetched.", todoId);

        return tasks;
    }

    public List<Task> getUncompletedTasks(Long todoId) throws AppEntityNotFoundException {

        Todo todo = getTodoById(todoId);
        log.info("Fetching all uncompleted tasks of todo of ID: {}...", todoId);
        List<Task> tasks = taskRepository.findByUncompletedTasks(todo);
        log.info("All uncompleted tasks of todo of ID: {} were successfully fetched.", todoId);

        return tasks;
    }

    public void addNewTodo(Todo todo, Long userId)
            throws AppEntityNotFoundException {

        log.info("Adding a new todo...");

        //Allocate a user to the new t0do.
        User user = userService.getUserById(userId);
        addNewUserTodo(todo, UserTodo.builder().user(user).build());

        todoRepository.save(todo);
        log.info("Todo of ID: {} was successfully added.", todo.getTodoId());
    }
    public void updateTodo(Todo updatedTodo, Long todoId) throws AppEntityNotFoundException {

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


    public void deleteTodo(Long todoId)
            throws AppEntityNotFoundException {

        Todo todo = getTodoById(todoId);
        log.info("Deleting todo of ID: {}...", todo.getTodoId());

        //Deleting the allocation of a user to this t0do
        deleteUserTodo(todo);

        Todo deletedTodo = todo;
        todoRepository.delete(todo);
        log.info("Todo of ID: {} was successfully deleted.", deletedTodo.getTodoId());
    }



    public void addNewUserTodo(Todo todo, UserTodo userTodo)
            throws AppEntityNotFoundException {

        log.info("Assigning todo of ID: {} to user of ID: {} and username: {}..."
                , todo.getTodoId(), userTodo.getUser().getUserId(), userTodo.getUser().getUsername());

        userTodoRepository.save(userTodo);
        todo.setUserTodo(userTodo);
        assignPositionToNewTodo(todo);
        log.info("Todo of ID: {} was successfully assigned to user of ID: {} and username: {}."
                , todo.getTodoId(), userTodo.getUser().getUserId(), userTodo.getUser().getUsername());
    }


    public void assignPositionToNewTodo(Todo todo) throws AppEntityNotFoundException {

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

    public void updateUserTodo(Long todoId, UserTodo updatedUserTodo) throws AppEntityNotFoundException {

        Todo todo = getTodoById(todoId);
        log.info("Updating information related to the assignment of todo of ID: {} to a user..."
                , todo.getTodoId());

        if (Objects.nonNull(updatedUserTodo.getPriorityLevel()) && !String.valueOf(updatedUserTodo.getPriorityLevel()).equals("")) {
            todo.getUserTodo().setPriorityLevel(updatedUserTodo.getPriorityLevel());
        }
        log.info("The information related to the assignment of todo of ID: {} to user of ID: {} was successfully updated."
                , todo.getTodoId(), todo.getUserTodo().getUser().getUserId());
    }


    void deleteUserTodo(Todo todo) throws AppEntityNotFoundException {
        log.info("Deleting allocation of user to todo of ID: {}..."
                , todo.getTodoId());

        userTodoRepository.delete(todo.getUserTodo());
        log.info("Allocation of user to todo of ID: {} was successfully deleted."
                , todo.getTodoId());
    }

    public void updateTodoPosition(Long todoId, int newPosition) throws AppEntityNotFoundException {

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

        log.info("Updating positions of all the todos that are between position {} and {}...", currentPosition, newPosition);
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
        log.info("The positions of all todos that are between position {} and {} were successfully updated."
                , currentPosition, newPosition);

        log.info("Updating the position of todo of ID: {}...", todo.getTodoId());
        // Update the position of the target todo
        todo.getUserTodo().setPosition(newPosition);
        todoRepository.save(todo);
        log.info("The position of todo of ID: {} was successfully updated.", todo.getTodoId());
    }
}
