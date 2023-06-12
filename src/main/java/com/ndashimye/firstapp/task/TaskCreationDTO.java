package com.ndashimye.firstapp.task;

import java.time.ZonedDateTime;

public record TaskCreationDTO(Long todoId,
                              Long parentTaskId,
                              String name,
                              ZonedDateTime dueTime,
                              Integer priorityLevel
) {
}
