package com.ndashimye.firstapp.task;

import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TaskDTOMapper implements Function<Task, TaskDTO> {
    @Override
    public TaskDTO apply(Task task) {
        return new TaskDTO(
                task.getTaskId(),
                task.getProject().getProjectId(),
                task.getParentTask() == null ? null : task.getParentTask().getTaskId(),
                task.getAssignedToUser() == null ? null : task.getAssignedToUser().getUser().getUserId(),
                task.getCompletedByUser() == null ? null : task.getCompletedByUser().getUser().getUserId(),
                task.getName(),
                task.getDescription(),
                task.getDueTime(),
                task.getCompletionTime(),
                task.getIsRecurrent(),
                task.getPriorityLevel(),
                task.getPosition(),
                task.getChildTasks().stream()
                        .map(this::apply)
                        .collect(Collectors.toList()));
    }
}
