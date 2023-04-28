package com.ndashimye.firstapp.todo;

import com.ndashimye.firstapp.task.Task;
import com.ndashimye.firstapp.usertodo.UserTodo;
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

    @PostMapping()
    public void addTodo(@RequestBody Todo todo) {

        todoService.addNewTodo(todo);
    }

    @PutMapping("/{todoId}")
    public void updateTodo(@PathVariable Long todoId, @RequestBody Todo updatedTodo)
            throws TodoNotFoundException {

        todoService.updateTodo(updatedTodo, todoId);
    }

    @DeleteMapping("/{todoId}")
    public void deleteTodo(@PathVariable Long todoId) throws TodoNotFoundException {

        todoService.deleteTodo(todoId);
    }



    @PostMapping("/{todoId}/user-todo")
    public void addUserTodo(@PathVariable Long todoId,
                              @RequestBody UserTodo userTodo)
            throws TodoNotFoundException {

        todoService.addNewUserTodo(todoId, userTodo);
    }

    @PutMapping("/{todoId}/user-todo")
    public void updateUserTodo(@PathVariable Long todoId,
                                 @RequestBody UserTodo updatedUserTodo)
            throws TodoNotFoundException {

        todoService.updateUserTodo(todoId, updatedUserTodo);
    }


    @PutMapping("/{todoId}/user-todo/update")
    public void updateUserTodoPosition(@PathVariable Long todoId,
                                         @RequestParam Integer position)
            throws TodoNotFoundException {

        todoService.updateTodoPosition(todoId, position);
    }

    @DeleteMapping("/{todoId}/user-todo")
    public void deleteUserTodo(@PathVariable Long todoId) throws TodoNotFoundException {

        Todo todo = todoService.getTodoById(todoId);
        Long id = todo.getUserTodo().getUserTodoId();
        todoService.deleteUserTodo(todoId);
    }
}
