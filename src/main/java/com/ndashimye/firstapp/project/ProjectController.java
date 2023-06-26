package com.ndashimye.firstapp.project;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.goal.GoalService;
import com.ndashimye.firstapp.goal.GoalCreationDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/projects")
@AllArgsConstructor
public class ProjectController {
    private final GoalService goalService;



    /*

    HTTP endpoints that handle all the operations
    related to the relationship between projects and goals

    */

    @PostMapping("/{projectId}/goals")
    public void addGoalToProject(@RequestBody GoalCreationDTO goalCreationDTO, @PathVariable Long projectId)
            throws AppEntityNotFoundException {

        goalService.addNewGoalToProject(goalCreationDTO, projectId);
    }
}
