package com.ndashimye.firstapp.userproject;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.project.Project;
import com.ndashimye.firstapp.project.ProjectCreationDTO;

import java.util.List;

public interface UserProjectService {

    /*
    Service methods that handle all the operations
    related to the relationship between users and their projects
    */
    UserProject getUserProjectByUserIdAndProjectId(Long userId, Long projectId)
            throws AppEntityNotFoundException;

    List<UserProjectDTO> getAllUserProjects(Long userId) throws AppEntityNotFoundException;

    void addNewProjectToUser(Long userId, ProjectCreationDTO project)
            throws AppEntityNotFoundException;

    void addExistingProjectToUser(Long userId, Long projectId)
            throws AppEntityNotFoundException;

    void updateProject(long userId, long projectId, ProjectCreationDTO updatedProject)
            throws AppEntityNotFoundException;

    void deleteProject(long userId, long projectId)
            throws AppEntityNotFoundException;

    void updateUserProjectRole(long userId, long projectId, String role)
            throws AppEntityNotFoundException;

    void assignPositionToNewProject(UserProject userProject);

    void updateProjectPosition(Long userId, Long projectId, int newPosition)
            throws AppEntityNotFoundException;
}
