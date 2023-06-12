package com.ndashimye.firstapp.task;

import java.time.ZonedDateTime;
import java.util.List;

public record TaskDTO(Long id,
                      Long todoId,
                      Long parentTaskId,
                      Long completedByUserId,
                      String name,
                      ZonedDateTime dueTime,
                      ZonedDateTime completionTime,
                      Integer priorityLevel,
                      Integer position,
                      List<TaskDTO> childTasks
                      )
{
}
