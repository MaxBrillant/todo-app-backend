package com.ndashimye.firstapp.todo;

import java.time.ZonedDateTime;

public record TodoDTO(Long id,
                      Long projectId,
                      String name,
                      String description,
                      ZonedDateTime dueTime,
                      Integer priorityLevel,
                      Integer position,
                      boolean isRecurrent,
                      ZonedDateTime dateCreated,
                      ZonedDateTime lastUpdated)
{
}
