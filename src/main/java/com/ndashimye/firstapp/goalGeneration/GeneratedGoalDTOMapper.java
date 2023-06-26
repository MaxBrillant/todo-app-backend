package com.ndashimye.firstapp.goalGeneration;

import com.ndashimye.firstapp.goal.GoalDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class GeneratedGoalDTOMapper implements Function<GoalDTO, GeneratedGoalDTO> {
    @Override
    public GeneratedGoalDTO apply(GoalDTO goalDTO) {
        return new GeneratedGoalDTO(
                goalDTO.projectId(),
                goalDTO.name(),
                goalDTO.description());
    }
}
