package com.ndashimye.firstapp.task;

import com.ndashimye.firstapp.todotask.TodoTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/{taskId}")
    public Task getTaskById(@PathVariable Long taskId) throws TaskNotFoundException {
        return taskService.getTaskById(taskId);
    }


    @GetMapping("/id/{taskId}/sub-tasks")
    public List<Task> getChildTasksByTaskId(@PathVariable Long taskId)
            throws TaskNotFoundException {

        return taskService.getAllChildTasksByTaskId(taskId);
    }

    @GetMapping("/id/{taskId}/sub-tasks/order-by/priority")
    public List<Task> getChildTasksByTaskIdOrderedByPriority(@PathVariable Long taskId)
            throws TaskNotFoundException {

        return taskService.getAllChildTasksByTaskIdOrderedByPriority(taskId);
    }

    @GetMapping("/id/{taskId}/sub-tasks/completed")
    public List<Task> getCompletedChildTasks(@PathVariable Long taskId)
            throws TaskNotFoundException {

        return taskService.getCompletedChildTasks(taskId);
    }

    @GetMapping("/id/{taskId}/sub-tasks/uncompleted")
    public List<Task> getUncompletedChildTasks(@PathVariable Long taskId)
            throws TaskNotFoundException {

        return taskService.getUncompletedChildTasks(taskId);
    }


    @PostMapping()
    public String addTask(@RequestBody Task task) {

        taskService.addNewTask(task);

        return "task of id "+task.getTaskId()+" was added successfully";
    }

    @PutMapping("/{taskId}")
    public String updateTask(@RequestBody Task updatedTask, @PathVariable Long taskId)
            throws TaskNotFoundException {

        Task task = taskService.getTaskById(taskId);

        taskService.updateTask(updatedTask, task);

        return "task of id "+task.getTaskId()+" was updated successfully";
    }

    @DeleteMapping("/{taskId}")
    public String deleteTask(@PathVariable Long taskId) throws TaskNotFoundException {

        Task task = taskService.getTaskById(taskId);
        Long id = task.getTaskId();
        taskService.deleteTask(task);

        return "task of id "+id+" was successfully deleted from the database";
    }



    @PostMapping("/{taskId}/todo-task")
    public String addTodoTask(@PathVariable Long taskId, @RequestBody TodoTask todoTask)
            throws TaskNotFoundException {

        taskService.addNewTodoTask(taskId, todoTask);

        return "todo task of id "+todoTask.getTodoTaskId()+" was added successfully";
    }

    @PutMapping("/{taskId}/todo-task")
    public String updateTodoTask(@PathVariable Long taskId,
                                 @RequestBody TodoTask updatedTodoTask)
            throws TaskNotFoundException {

        taskService.updateTodoTask(taskId, updatedTodoTask);

        return "todo task of id "+updatedTodoTask.getTodoTaskId()+" was updated successfully";
    }


    @PutMapping("/{taskId}/todo-task/update")
    public String updateTodoTaskPosition(@PathVariable Long taskId,
                                         @RequestParam Integer position)
            throws TaskNotFoundException {

        taskService.updateTaskPosition(taskId, position);

        return "position of todo task that belongs to the task of id "+taskId+" was updated successfully";
    }

    @DeleteMapping("/{taskId}/todo-task")
    public String deleteTodoTask(@PathVariable Long taskId) throws TaskNotFoundException {

        Task task = taskService.getTaskById(taskId);
        Long id = task.getTodoTask().getTodoTaskId();
        taskService.deleteTodoTask(taskId);

        return "todo task of id "+id+" was successfully deleted from the database";
    }
}
