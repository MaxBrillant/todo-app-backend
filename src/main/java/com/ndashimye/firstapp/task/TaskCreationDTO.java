package com.ndashimye.firstapp.task;

import java.time.ZonedDateTime;

public record TaskCreationDTO(Long parentTaskId,
                              String name,
                              String description,
                              ZonedDateTime dueTime,
                              boolean isRecurrent,
                              Integer priorityLevel
) {
}
