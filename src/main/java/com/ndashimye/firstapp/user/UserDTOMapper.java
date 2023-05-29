package com.ndashimye.firstapp.user;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(user.getUserId(),
                user.getUsername(),
                user.getEmailAddress(),
                user.getProfile().getFirstName(),
                user.getProfile().getLastName(),
                user.getProfile().getProfileImageUrl(),
                user.getSettings().getLanguage(),
                user.getSettings().getTheme(),
                user.getSettings().getTimeZone(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }
}
