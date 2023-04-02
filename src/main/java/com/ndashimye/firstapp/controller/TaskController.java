package com.ndashimye.firstapp.controller;
import com.ndashimye.firstapp.error.TaskNotFoundException;
import com.ndashimye.firstapp.model.Task;
import com.ndashimye.firstapp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/{taskId}")
    public Task getTaskById(@PathVariable Integer taskId) throws TaskNotFoundException {
        return taskService.getTaskById(taskId);
    }

    @PostMapping()
    public String addTask(@RequestBody Task task) {

        taskService.addNewTask(task);

        return "task of id "+task.getTaskId()+" was added successfully";
    }

    @PutMapping("/{taskId}")
    public String updateTask(@RequestBody Task updatedTask, @PathVariable Integer taskId)
            throws TaskNotFoundException {

        Task task = taskService.getTaskById(taskId);

        taskService.updateTask(updatedTask, task);

        return "task of id "+task.getTaskId()+" was updated successfully";
    }

    @DeleteMapping("/{taskId}")
    public String deleteTask(@PathVariable Integer taskId) throws TaskNotFoundException {

        Task task = taskService.getTaskById(taskId);
        int id = task.getTaskId();
        taskService.deleteTask(task);
        return "task of id "+id+" was successfully deleted from the database";
    }
}
