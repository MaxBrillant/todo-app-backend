package com.ndashimye.firstapp.taskGeneration;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class TaskGenerationController {
    private TaskGenerationService taskGenerationService;

    @GetMapping("/projects/{projectId}/generate/tasks")
    public List<GeneratedTaskDTO> generateTasksOfProject(@PathVariable Long projectId,
                                                      @RequestParam("number") Integer numberOfTasks)
            throws AppEntityNotFoundException {

        return taskGenerationService.generateTasksOfProject(projectId, numberOfTasks);
    }

    @GetMapping("/tasks/{taskId}/generate/sub-tasks")
    public List<GeneratedTaskDTO> generateChildTasksOfTask(@PathVariable Long taskId,
                                                           @RequestParam("number") Integer numberOfChildTasks)
            throws AppEntityNotFoundException {

        return taskGenerationService.generateChildTasksOfTask(taskId, numberOfChildTasks);
    }
}
