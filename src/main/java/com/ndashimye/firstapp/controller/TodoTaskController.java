package com.ndashimye.firstapp.controller;


import com.ndashimye.firstapp.error.TodoTaskNotFoundException;
import com.ndashimye.firstapp.model.TodoTask;
import com.ndashimye.firstapp.service.TodoTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/todo-tasks")
public class TodoTaskController {

    @Autowired
    private TodoTaskService todoTaskService;

    @GetMapping("/{todoTaskId}")
    public TodoTask getTodoTaskById(@PathVariable Integer todoTaskId)
            throws TodoTaskNotFoundException {

        return todoTaskService.getTodoTaskById(todoTaskId);
    }

    @PostMapping()
    public String addTodoTask(@RequestBody TodoTask todoTask) {

        todoTaskService.addNewTodoTask(todoTask);

        return "todo task of id "+todoTask.getTodoTaskId()+" was added successfully";
    }

    @PutMapping("/{todoTaskId}")
    public String updateTodoTask(@RequestBody TodoTask updatedTodoTask,
                                 @PathVariable Integer todoTaskId) throws TodoTaskNotFoundException {

        TodoTask todoTask = todoTaskService.getTodoTaskById(todoTaskId);

        todoTaskService.updateTodoTask(updatedTodoTask, todoTask);

        return "todo task of id "+todoTask.getTodoTaskId()+" was updated successfully";
    }

    @DeleteMapping("/{todoTaskId}")
    public String deleteTodoTask(@PathVariable Integer todoTaskId) throws TodoTaskNotFoundException {

        TodoTask todoTask = todoTaskService.getTodoTaskById(todoTaskId);
        int id = todoTask.getTodoTaskId();
        todoTaskService.deleteTodoTask(todoTask);
        return "todo task of id "+id+" was successfully deleted from the database";
    }
}
