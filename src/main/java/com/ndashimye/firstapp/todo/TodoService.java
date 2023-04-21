package com.ndashimye.firstapp.todo;

import com.ndashimye.firstapp.task.Task;
import com.ndashimye.firstapp.task.TaskRepository;
import com.ndashimye.firstapp.usertodo.UserTodo;
import com.ndashimye.firstapp.usertodo.UserTodoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserTodoRepository userTodoRepository;

    @Autowired
    private TaskRepository taskRepository;

    public Todo getTodoById(Long todoId) throws TodoNotFoundException {

        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new TodoNotFoundException());
        return todo;

    }


    public List<Task> getAllTasksByTodoId(Long todoId) throws TodoNotFoundException {

        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new TodoNotFoundException());
        List<Task> tasks = taskRepository.findByTodoTask_TodoOrderByTodoTask_PositionAsc(todo);

        return tasks;
    }


    public List<Task> getAllTasksByTodoIdOrderedByPriority(Long todoId) throws TodoNotFoundException {

        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new TodoNotFoundException());
        List<Task> tasks = taskRepository.findByTodoTask_TodoOrderByTodoTask_PriorityLevelDesc(todo);

        return tasks;
    }

    public List<Task> getCompletedTasks(Long todoId) throws TodoNotFoundException {

        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new TodoNotFoundException());
        List<Task> tasks = taskRepository.findByCompletedTasks(todo);

        return tasks;
    }

    public List<Task> getUncompletedTasks(Long todoId) throws TodoNotFoundException {

        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new TodoNotFoundException());
        List<Task> tasks = taskRepository.findByUncompletedTasks(todo);

        return tasks;
    }

    public void addNewTodo(Todo todo) {
        todoRepository.save(todo);
    }

    public void updateTodo(Todo updatedTodo, Todo todo) {

//        if(Objects.nonNull(updatedTodo.getUserTodo())) {
//            if(!updatedTodo.getUserTodo().equals("")) {
//                todo.setUserTodo(updatedTodo.getUserTodo());
//            }
//        }else {
//            userTodoRepository.delete(todo.getUserTodo());
//        }

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
    }

    public void deleteTodo(Todo todo) {
        todoRepository.delete(todo);
    }



    public void addNewUserTodo(Long todoId, UserTodo userTodo) throws TodoNotFoundException {

        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new TodoNotFoundException());

        if (Objects.isNull(todo.getUserTodo())) {
            userTodoRepository.save(userTodo);
            todo.setUserTodo(userTodo);
            assignPositionToNewTodo(todo);
        }
    }


    public void assignPositionToNewTodo(Todo todo) {
        // Get the maximum position value from all existing todos
        Integer maxPosition = todoRepository.getMaxPosition();

        // If there are no existing todos, set the position to 1
        if (maxPosition == null) {
            maxPosition = 0;
        }

        // Assign the new todo's position to be the maximum position + 1
        todo.getUserTodo().setPosition(maxPosition + 1);

        // Save the new todo
        todoRepository.save(todo);
    }

    public void updateUserTodo(Long todoId, UserTodo updatedUserTodo) throws TodoNotFoundException {

        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new TodoNotFoundException());

        if (Objects.nonNull(todo.getUserTodo())) {
            if (Objects.nonNull(updatedUserTodo.getUser()) && !updatedUserTodo.getUser().equals("")) {
                todo.getUserTodo().setUser(updatedUserTodo.getUser());
            }
            if (Objects.nonNull(updatedUserTodo.getPriorityLevel()) && !String.valueOf(updatedUserTodo.getPriorityLevel()).equals("")) {
                todo.getUserTodo().setPriorityLevel(updatedUserTodo.getPriorityLevel());
            }
        }
    }


    public void updateTodoPosition(Long todoId, int newPosition) throws TodoNotFoundException {

        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new TodoNotFoundException());

        // Get the current position of the todo
        int currentPosition = todo.getUserTodo().getPosition();

        // If the new position is equal to the current position, do nothing
        if (newPosition == currentPosition) {
            return;
        }

        // Get the todos with positions between the current and new positions
        List<Todo> todosToUpdate;
        if (newPosition > currentPosition) {
            todosToUpdate = todoRepository.findTodosWithPositionsBetween(currentPosition + 1, newPosition);
        } else {
            todosToUpdate = todoRepository.findTodosWithPositionsBetween(newPosition, currentPosition - 1);
        }

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

        // Update the position of the target todo
        todo.getUserTodo().setPosition(newPosition);
        todoRepository.save(todo);
    }

    public void deleteUserTodo(Long todoId) throws TodoNotFoundException {

        Todo todo = todoRepository.findById(todoId).orElseThrow(() -> new TodoNotFoundException());

        if (Objects.nonNull(todo.getUserTodo())) {
            userTodoRepository.delete(todo.getUserTodo());
        }
    }
}
