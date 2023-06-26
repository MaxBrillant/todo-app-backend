package com.ndashimye.firstapp.goal;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class GoalDTOMapper implements Function<Goal, GoalDTO> {
    @Override
    public GoalDTO apply(Goal goal) {
        return new GoalDTO(
                goal.getGoalId(),
                goal.getProject().getProjectId(),
                goal.getName(),
                goal.getDescription(),
                goal.getDueTime(),
                goal.getPosition(),
                goal.getCreatedAt(),
                goal.getUpdatedAt());
    }
}
