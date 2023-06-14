package com.ndashimye.firstapp.taskGeneration;

import com.ndashimye.firstapp.task.TaskDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class GeneratedTaskDTOMapper implements Function<TaskDTO, GeneratedTaskDTO> {
    @Override
    public GeneratedTaskDTO apply(TaskDTO taskDTO) {
        return new GeneratedTaskDTO(
                taskDTO.todoId(),
                taskDTO.name(),
                taskDTO.priorityLevel());
    }
}
