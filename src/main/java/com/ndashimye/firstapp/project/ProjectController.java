package com.ndashimye.firstapp.project;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.task.TaskCreationDTO;
import com.ndashimye.firstapp.task.TaskDTO;
import com.ndashimye.firstapp.task.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/projects")
@AllArgsConstructor
public class ProjectController {
    private final TaskService taskService;



    /*

    HTTP endpoints that handle all the operations
    related to the relationship between projects and tasks

    */

    @PostMapping("/{projectId}/tasks")
    public void addTaskToProject(@RequestBody TaskCreationDTO taskCreationDTO, @PathVariable Long projectId)
            throws AppEntityNotFoundException {

        taskService.addNewTaskToProject(taskCreationDTO, projectId);
    }

    @GetMapping("/{projectId}/tasks")
    public List<TaskDTO> getTasksByProjectId(@PathVariable Long projectId)
            throws AppEntityNotFoundException {

        return taskService.getAllTasksByProjectId(projectId);
    }

    @GetMapping("/{projectId}/tasks/order-by/priority")
    public List<TaskDTO> getTasksByProjectIdOrderedByPriority
            (@PathVariable Long projectId)
            throws AppEntityNotFoundException {

        return taskService.getAllTasksByProjectIdOrderedByPriority(projectId);
    }

    @GetMapping("/{projectId}/tasks/completed")
    public List<TaskDTO> getCompletedTasks(@PathVariable Long projectId)
            throws AppEntityNotFoundException {

        return taskService.getCompletedTasksInProject(projectId);
    }

    @GetMapping("/{projectId}/tasks/uncompleted")
    public List<TaskDTO> getUncompletedTasks(@PathVariable Long projectId)
            throws AppEntityNotFoundException {

        return taskService.getUncompletedTasksInProject(projectId);
    }
}
