package com.ndashimye.firstapp.userproject;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.project.Project;
import com.ndashimye.firstapp.project.ProjectCreationDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users/{userId}/projects")
@AllArgsConstructor
public class UserProjectController {

    private final UserProjectService userProjectService;



    /*

    HTTP endpoints that handle all the operations
    related to the relationship between users and their projects

    */

    @PostMapping()
    public void addNewProjectToUser(@PathVariable Long userId,
                           @RequestBody ProjectCreationDTO projectCreationDTO)
            throws AppEntityNotFoundException {

        userProjectService.addNewProjectToUser(userId, projectCreationDTO);
    }

    @PostMapping("/{projectId}")
    public void addExistingProjectToUser(@PathVariable Long userId,
                                 @PathVariable Long projectId)
            throws AppEntityNotFoundException {

        userProjectService.addExistingProjectToUser(userId, projectId);
    }

    @PutMapping("/{projectId}")
    public void updateProject(@PathVariable Long userId,
                              @PathVariable Long projectId,
                              @RequestBody ProjectCreationDTO updatedProject)
            throws AppEntityNotFoundException {

        userProjectService.updateProject(userId, projectId, updatedProject);
    }

    @DeleteMapping("/{projectId}")
    public void deleteProject(@PathVariable Long userId,
                              @PathVariable Long projectId)
            throws AppEntityNotFoundException {

        userProjectService.deleteProject(userId, projectId);
    }

    @PutMapping("/{projectId}/update/role")
    public void updateProjectRole(@PathVariable Long userId,
                                      @PathVariable Long projectId,
                                      @RequestParam String role)
            throws AppEntityNotFoundException {

        userProjectService.updateUserProjectRole(userId, projectId, role);
    }

    @PutMapping("/{projectId}/update/position")
    public void updateProjectPosition(@PathVariable Long userId,
                           @PathVariable Long projectId,
                           @RequestParam int newPosition)
            throws AppEntityNotFoundException {

        userProjectService.updateProjectPosition(userId, projectId, newPosition);
    }
}
