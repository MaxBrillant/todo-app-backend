package com.ndashimye.firstapp.todo;

import com.ndashimye.firstapp.task.Task;
import com.ndashimye.firstapp.user.UserNotFoundException;
import com.ndashimye.firstapp.usersettings.UserSettingsNotFoundException;
import com.ndashimye.firstapp.usertodo.UserTodo;
import com.ndashimye.firstapp.usertodo.UserTodoNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/{todoId}")
    public Todo getTodoById(@PathVariable Long todoId) throws TodoNotFoundException {
        return todoService.getTodoById(todoId);
    }


    @GetMapping("/id/{todoId}/tasks")
    public List<Task> getTasksByTodoId(@PathVariable Long todoId) throws TodoNotFoundException {
        return todoService.getAllTasksByTodoId(todoId);
    }

    @GetMapping("/id/{todoId}/tasks/order-by/priority")
    public List<Task> getTasksByTodoIdOrderedByPriority(@PathVariable Long todoId)
            throws TodoNotFoundException {

        return todoService.getAllTasksByTodoIdOrderedByPriority(todoId);
    }

    @GetMapping("/id/{todoId}/tasks/completed")
    public List<Task> getCompletedTasks(@PathVariable Long todoId) throws TodoNotFoundException {
        return todoService.getCompletedTasks(todoId);
    }

    @GetMapping("/id/{todoId}/tasks/uncompleted")
    public List<Task> getUncompletedTasks(@PathVariable Long todoId) throws TodoNotFoundException {
        return todoService.getUncompletedTasks(todoId);
    }

    @PostMapping("/{userId}")
    public void addTodo(@RequestBody Todo todo, @PathVariable Long userId)
            throws UserNotFoundException, UserTodoNotFoundException {

        todoService.addNewTodo(todo, userId);
    }

    @PutMapping("/{todoId}")
    public void updateTodo(@PathVariable Long todoId, @RequestBody Todo updatedTodo)
            throws TodoNotFoundException, UserNotFoundException, UserSettingsNotFoundException
            , UserTodoNotFoundException {

        todoService.updateTodo(updatedTodo, todoId);
    }

    @DeleteMapping("/{todoId}")
    public void deleteTodo(@PathVariable Long todoId)
            throws TodoNotFoundException, UserTodoNotFoundException {

        todoService.deleteTodo(todoId);
    }

    @PutMapping("/{todoId}/user-todo")
    public void updateUserTodo(@PathVariable Long todoId,
                                 @RequestBody UserTodo updatedUserTodo)
            throws TodoNotFoundException, UserNotFoundException, UserTodoNotFoundException {

        todoService.updateUserTodo(todoId, updatedUserTodo);
    }


    @PutMapping("/{todoId}/user-todo/update")
    public void updateUserTodoPosition(@PathVariable Long todoId,
                                         @RequestParam Integer position)
            throws TodoNotFoundException, UserTodoNotFoundException {

        todoService.updateTodoPosition(todoId, position);
    }
}
