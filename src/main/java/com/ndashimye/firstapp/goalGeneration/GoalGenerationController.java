package com.ndashimye.firstapp.goalGeneration;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/users/{userId}/projects/{projectId}/generate/goals")
public class GoalGenerationController {
    private final GoalGenerationService goalGenerationService;

    @GetMapping()
    public List<GeneratedGoalDTO> generateGoalsOfProject(@PathVariable Long userId,
                                                         @PathVariable Long projectId,
                                                         @RequestParam("number") Integer numberOfGoals)
            throws AppEntityNotFoundException, IllegalAccessException {

        return goalGenerationService.generateGoalsOfProject(userId, projectId, numberOfGoals);
    }
}
