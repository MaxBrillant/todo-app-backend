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
                task.getTodo().getTodoId(),
                task.getParentTask() == null ? null : task.getParentTask().getTaskId(),
                task.getCompletedByUser() == null ? null : task.getCompletedByUser().getUserId(),
                task.getName(),
                task.getDueTime(),
                task.getCompletionTime(),
                task.getPriorityLevel(),
                task.getPosition(),
                task.getChildTasks().stream()
                        .map(this::apply)
                        .collect(Collectors.toList()));
    }
}
