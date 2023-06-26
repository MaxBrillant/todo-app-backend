package com.ndashimye.firstapp.goal;

import java.time.ZonedDateTime;

public record GoalCreationDTO(String name,
                              String description,
                              ZonedDateTime dueTime)
{
}
