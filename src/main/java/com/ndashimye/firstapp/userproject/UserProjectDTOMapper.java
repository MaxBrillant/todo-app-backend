package com.ndashimye.firstapp.userproject;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class UserProjectDTOMapper implements Function<UserProject, UserProjectDTO> {
    @Override
    public UserProjectDTO apply(UserProject userProject) {
        return new UserProjectDTO(
                userProject.getProject().getProjectId(),
                userProject.getProject().getName(),
                userProject.getProject().getDescription(),
                userProject.getProject().getCoverImageUrl(),
                userProject.getProjectRole(),
                userProject.getPosition(),
                userProject.getProject().getCreatedAt(),
                userProject.getProject().getUpdatedAt());
    }
}
