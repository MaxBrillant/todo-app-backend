package com.ndashimye.firstapp.user;

import com.ndashimye.firstapp.usersettings.Language;
import com.ndashimye.firstapp.usersettings.Theme;

import java.time.ZonedDateTime;

public record UserDTO(Long id,
                      String username,
                      String email,
                      String firstName,
                      String lastName,
                      String profileURL,
                      Language language,
                      Theme theme,
                      String timezone,
                      ZonedDateTime dateCreated,
                      ZonedDateTime lastUpdated)
{
}
