package com.ndashimye.firstapp.userproject;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.project.Project;

public interface UserProjectService {

    /*
    Service methods that handle all the operations
    related to the relationship between users and their projects
    */
    UserProject getUserProjectByUserIdAndProjectId(Long userId, Long projectId)
            throws AppEntityNotFoundException;

    void addNewProjectToUser(Long userId, Project project)
            throws AppEntityNotFoundException;

    void addExistingProjectToUser(Long userId, Long projectId)
            throws AppEntityNotFoundException;

    void updateUserProjectRole(long userId, long projectId, String role)
            throws AppEntityNotFoundException;

    void assignPositionToNewProject(UserProject userProject);

    void updateProjectPosition(Long userId, Long projectId, int newPosition)
            throws AppEntityNotFoundException;
}
