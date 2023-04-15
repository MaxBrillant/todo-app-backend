package com.ndashimye.firstapp.task;

import com.ndashimye.firstapp.todo.TodoNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/{taskId}")
    public Task getTaskById(@PathVariable Integer taskId) throws TaskNotFoundException {
        return taskService.getTaskById(taskId);
    }


    @GetMapping("/id/{taskId}/sub-tasks")
    public List<Task> getChildTasksByTaskId(@PathVariable Integer taskId)
            throws TaskNotFoundException {

        return taskService.getAllChildTasksByTaskId(taskId);
    }

    @GetMapping("/id/{taskId}/sub-tasks/order-by/priority")
    public List<Task> getChildTasksByTaskIdOrderedByPriority(@PathVariable Integer taskId)
            throws TaskNotFoundException {

        return taskService.getAllChildTasksByTaskIdOrderedByPriority(taskId);
    }

    @GetMapping("/id/{taskId}/sub-tasks/completed")
    public List<Task> getCompletedChildTasks(@PathVariable Integer taskId)
            throws TaskNotFoundException {

        return taskService.getCompletedChildTasks(taskId);
    }

    @GetMapping("/id/{taskId}/sub-tasks/uncompleted")
    public List<Task> getUncompletedChildTasks(@PathVariable Integer taskId)
            throws TaskNotFoundException {

        return taskService.getUncompletedChildTasks(taskId);
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
