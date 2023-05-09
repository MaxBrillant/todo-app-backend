package com.ndashimye.firstapp.task;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
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
    public Task getTaskById(@PathVariable Long taskId) throws AppEntityNotFoundException {
        return taskService.getTaskById(taskId);
    }


    @GetMapping("/id/{taskId}/sub-tasks")
    public List<Task> getChildTasksByTaskId(@PathVariable Long taskId)
            throws AppEntityNotFoundException {

        return taskService.getAllChildTasksByTaskId(taskId);
    }

    @GetMapping("/id/{taskId}/sub-tasks/order-by/priority")
    public List<Task> getChildTasksByTaskIdOrderedByPriority(@PathVariable Long taskId)
            throws AppEntityNotFoundException {

        return taskService.getAllChildTasksByTaskIdOrderedByPriority(taskId);
    }

    @GetMapping("/id/{taskId}/sub-tasks/completed")
    public List<Task> getCompletedChildTasks(@PathVariable Long taskId)
            throws AppEntityNotFoundException {

        return taskService.getCompletedChildTasks(taskId);
    }

    @GetMapping("/id/{taskId}/sub-tasks/uncompleted")
    public List<Task> getUncompletedChildTasks(@PathVariable Long taskId)
            throws AppEntityNotFoundException {

        return taskService.getUncompletedChildTasks(taskId);
    }


    @PostMapping("/{todoId}")
    public void addTask(@RequestBody Task task, @PathVariable Long todoId)
            throws AppEntityNotFoundException {

        taskService.addNewTask(task, todoId);
    }

    @PutMapping("/{taskId}")
    public void updateTask(@RequestBody Task updatedTask, @PathVariable Long taskId)
            throws AppEntityNotFoundException {

        taskService.updateTask(updatedTask, taskId);
    }

    @DeleteMapping("/{taskId}")
    public void deleteTask(@PathVariable Long taskId) throws AppEntityNotFoundException {
        taskService.deleteTask(taskId);
    }

    @PutMapping("/{taskId}/parent-task/{parentTaskId}/position")
    public void updateParentTask(@PathVariable Long taskId,
                               @PathVariable Long parentTaskId, @RequestParam int position)
            throws AppEntityNotFoundException {

        taskService.updateParentTask(taskId, parentTaskId, position);
    }

    @PutMapping("/{taskId}/todo-task")
    public void updateTodoTask(@PathVariable Long taskId,
                                 @RequestBody TodoTask updatedTodoTask)
            throws AppEntityNotFoundException {

        taskService.updateTodoTask(taskId, updatedTodoTask);
    }


    @PutMapping("/{taskId}/todo-task/update")
    public void updateTodoTaskPosition(@PathVariable Long taskId,
                                         @RequestParam Integer position)
            throws AppEntityNotFoundException {

        taskService.updateTaskPosition(taskId, position);
    }
}
