package com.ndashimye.firstapp.todo;

import java.time.ZonedDateTime;

public record TodoCreationDTO(String name,
                              String description,
                              ZonedDateTime dueTime)
{
}
