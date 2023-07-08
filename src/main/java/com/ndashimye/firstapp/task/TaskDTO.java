package com.ndashimye.firstapp.task;

import java.time.ZonedDateTime;
import java.util.List;

public record TaskDTO(Long id,
                      Long projectId,
                      Long parentTaskId,
                      Long assignedToUserId,
                      Long completedByUserId,
                      String name,
                      String description,
                      ZonedDateTime dueTime,
                      ZonedDateTime completionTime,
                      boolean isRecurrent,
                      Integer priorityLevel,
                      Integer position,
                      List<TaskDTO> childTasks
                      )
{
}
