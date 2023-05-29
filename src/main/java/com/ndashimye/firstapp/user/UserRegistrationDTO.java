package com.ndashimye.firstapp.user;

import com.ndashimye.firstapp.usersettings.Language;
import com.ndashimye.firstapp.usersettings.Theme;

public record UserRegistrationDTO(String username,
                                  String email,
                                  String password,
                                  String firstName,
                                  String lastName,
                                  String profileURL,
                                  Language language,
                                  Theme theme,
                                  String timezone) {
}
