package com.ndashimye.firstapp.userproject;

import java.time.ZonedDateTime;

public record UserProjectDTO(Long id,
                             String name,
                             String description,
                             ProjectRole role,
                             Integer position,
                             ZonedDateTime dateCreated,
                             ZonedDateTime lastUpdated) {
}
