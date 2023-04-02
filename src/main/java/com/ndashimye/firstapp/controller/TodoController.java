package com.ndashimye.firstapp.controller;

import com.ndashimye.firstapp.error.InvalidTimeFormatException;
import com.ndashimye.firstapp.error.TodoNotFoundException;
import com.ndashimye.firstapp.error.UserNotFoundException;
import com.ndashimye.firstapp.model.Task;
import com.ndashimye.firstapp.model.Todo;
import com.ndashimye.firstapp.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/v1/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/{todoId}")
    public Todo getTodoById(@PathVariable Integer todoId) throws TodoNotFoundException {
        return todoService.getTodoById(todoId);
    }


    @GetMapping("/id/{todoId}/tasks")
    public List<Task> getTasksByTodoId(@PathVariable Integer todoId) throws TodoNotFoundException {
        return todoService.getAllTasksByTodoId(todoId);
    }

    @GetMapping("/id/{todoId}/tasks/order-by/priority")
    public List<Task> getTasksByTodoIdOrderedByPriority(@PathVariable Integer todoId) throws TodoNotFoundException {
        return todoService.getAllTasksByTodoIdOrderedByPriority(todoId);
    }

    @GetMapping("/id/{todoId}/tasks/completed")
    public List<Task> getCompletedTasks(@PathVariable Integer todoId) throws TodoNotFoundException {
        return todoService.getCompletedTasks(todoId);
    }

    @GetMapping("/id/{todoId}/tasks/uncompleted")
    public List<Task> getUncompletedTasks(@PathVariable Integer todoId) throws TodoNotFoundException {
        return todoService.getUncompletedTasks(todoId);
    }

    @PostMapping()
    public String addTodo(@RequestBody Todo todo) {

        todoService.addNewTodo(todo);

        return "todo of id "+todo.getTodoId()+" was added successfully";
    }

    @PutMapping("/{todoId}")
    public String updateTodo(@RequestBody Todo updatedTodo, @PathVariable Integer todoId) throws TodoNotFoundException {

        Todo todo = todoService.getTodoById(todoId);

        todoService.updateTodo(updatedTodo, todo);

        return "todo of id "+todo.getTodoId()+" was updated successfully";
    }

    @DeleteMapping("/{todoId}")
    public String deleteTodo(@PathVariable Integer todoId) throws TodoNotFoundException {

        Todo todo = todoService.getTodoById(todoId);
        int id = todo.getTodoId();
        todoService.deleteTodo(todo);
        return "todo of id "+id+" was successfully deleted from the database";
    }
}
