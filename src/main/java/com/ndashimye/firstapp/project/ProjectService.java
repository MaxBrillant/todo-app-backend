package com.ndashimye.firstapp.project;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import java.util.List;

public interface ProjectService {

    //Service methods that handle all the operations related to projects
    Project getProjectById(Long projectId) throws AppEntityNotFoundException;

    void updateProject(Long projectId, Project updatedProject)
            throws AppEntityNotFoundException;

    void deleteProject(Long projectId) throws AppEntityNotFoundException;

    /*
    Service methods that handle all the operations
    related to the relationship between projects and users
    */
    List<Project> getAllProjectsByUserId(Long userId) throws AppEntityNotFoundException;
}
