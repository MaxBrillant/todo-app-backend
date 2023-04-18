package com.ndashimye.firstapp.task;

import com.ndashimye.firstapp.todotask.TodoTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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



    @PostMapping("/{taskId}/todo-task")
    public String addTodoTask(@PathVariable Integer taskId, @RequestBody TodoTask todoTask)
            throws TaskNotFoundException {

        taskService.addNewTodoTask(taskId, todoTask);

        return "todo task of id "+todoTask.getTodoTaskId()+" was added successfully";
    }

    @PutMapping("/{taskId}/todo-task")
    public String updateTodoTask(@PathVariable Integer taskId,
                                 @RequestBody TodoTask updatedTodoTask)
            throws TaskNotFoundException {

        taskService.updateTodoTask(taskId, updatedTodoTask);

        return "todo task of id "+updatedTodoTask.getTodoTaskId()+" was updated successfully";
    }


    @PutMapping("/{taskId}/todo-task/update")
    public String updateTodoTaskPosition(@PathVariable Integer taskId,
                                         @RequestParam Integer position)
            throws TaskNotFoundException {

        taskService.updateTaskPosition(taskId, position);

        return "position of todo task that belongs to the task of id "+taskId+" was updated successfully";
    }

    @DeleteMapping("/{taskId}/todo-task")
    public String deleteTodoTask(@PathVariable Integer taskId) throws TaskNotFoundException {

        Task task = taskService.getTaskById(taskId);
        int id = task.getTodoTask().getTodoTaskId();
        taskService.deleteTodoTask(taskId);

        return "todo task of id "+id+" was successfully deleted from the database";
    }
}
