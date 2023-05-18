package com.ndashimye.firstapp.task;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@AllArgsConstructor
public class TaskController {

    private TaskService taskService;



    /*

    HTTP endpoints that handle all the operations related to projects

    */

    @PutMapping("/{taskId}")
    public void updateTask(@RequestBody Task updatedTask, @PathVariable Long taskId)
            throws AppEntityNotFoundException {

        taskService.updateTask(updatedTask, taskId);
    }

    @DeleteMapping("/{taskId}")
    public void deleteTask(@PathVariable Long taskId) throws AppEntityNotFoundException {
        taskService.deleteTask(taskId);
    }

    @PutMapping("/{taskId}/uncompleted")
    public void unCompleteTask(@PathVariable Long taskId)
            throws AppEntityNotFoundException {

        taskService.unCompleteTask(taskId);
    }

    @PutMapping("/{taskId}/update/position")
    public void updateTaskPosition(@PathVariable Long taskId,
                                   @RequestParam Integer position)
            throws AppEntityNotFoundException {

        taskService.updateTaskPosition(taskId, position);
    }

    @GetMapping("/{taskId}")
    public Task getTaskById(@PathVariable Long taskId) throws AppEntityNotFoundException {
        return taskService.getTaskById(taskId);
    }



    /*

    HTTP endpoints that handle all the operations
    related to the relationship between tasks and their parent tasks

    */

    @PutMapping("/{taskId}/parent-task/{parentTaskId}/update")
    public void updateParentTask(@PathVariable Long taskId,
                                 @PathVariable Long parentTaskId, @RequestParam int position)
            throws AppEntityNotFoundException {

        taskService.updateParentTask(taskId, parentTaskId, position);
    }



    /*

    HTTP endpoints that handle all the operations
    related to the relationship between tasks and their children tasks (sub-tasks)

    */

    @GetMapping("/{taskId}/sub-tasks")
    public List<Task> getChildTasksByTaskId(@PathVariable Long taskId)
            throws AppEntityNotFoundException {

        return taskService.getAllChildTasksByTaskId(taskId);
    }

    @GetMapping("/{taskId}/sub-tasks/order-by/priority")
    public List<Task> getChildTasksByTaskIdOrderedByPriority(@PathVariable Long taskId)
            throws AppEntityNotFoundException {

        return taskService.getAllChildTasksByTaskIdOrderedByPriority(taskId);
    }

    @GetMapping("/{taskId}/sub-tasks/completed")
    public List<Task> getCompletedChildTasks(@PathVariable Long taskId)
            throws AppEntityNotFoundException {

        return taskService.getCompletedChildTasks(taskId);
    }

    @GetMapping("/{taskId}/sub-tasks/uncompleted")
    public List<Task> getUncompletedChildTasks(@PathVariable Long taskId)
            throws AppEntityNotFoundException {

        return taskService.getUncompletedChildTasks(taskId);
    }
}
