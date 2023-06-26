package com.ndashimye.firstapp.goal;

import java.time.ZonedDateTime;

public record GoalDTO(Long id,
                      Long projectId,
                      String name,
                      String description,
                      ZonedDateTime dueTime,
                      Integer position,
                      ZonedDateTime dateCreated,
                      ZonedDateTime lastUpdated)
{
}
