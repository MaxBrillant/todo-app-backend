package com.ndashimye.firstapp.project;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.user.User;
import com.ndashimye.firstapp.user.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final UserService userService;
    private ProjectRepository projectRepository;



    //Service methods that handle all the operations related to projects

    @Override
    public Project getProjectById(Long projectId) throws AppEntityNotFoundException {
        log.info("Fetching project by ID: {}...", projectId);
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new AppEntityNotFoundException(Project.class));
        log.info("Project of ID: {} was successfully fetched.", project.getProjectId());

        return project;
    }

    @Override
    public void updateProject(Long projectId, Project updatedProject) throws AppEntityNotFoundException {

        Project project = getProjectById(projectId);
        log.info("Updating project of ID: {}...", project.getProjectId());

        if (Objects.nonNull(updatedProject.getName()) && !updatedProject.getName().equals("")) {
            project.setName(updatedProject.getName());
        }
        if (Objects.nonNull(updatedProject.getDescription()) && !updatedProject.getDescription().equals("")) {
            project.setDescription(updatedProject.getDescription());
        }
        if (Objects.nonNull(updatedProject.getCoverImageUrl()) && !updatedProject.getCoverImageUrl().equals("")) {
            project.setCoverImageUrl(updatedProject.getCoverImageUrl());
        }

        projectRepository.save(project);
        log.info("Project of ID: {} was successfully updated.", project.getProjectId());
    }

    @Override
    public void deleteProject(Long projectId)
            throws AppEntityNotFoundException {

        Project project = getProjectById(projectId);
        log.info("Deleting project of ID: {}...", project.getProjectId());
        Project deletedProject = project;
        projectRepository.delete(project);
        log.info("Project of ID: {} was successfully deleted.", deletedProject.getProjectId());
    }



    /*

    Service methods that handle all the operations
    related to the relationship between projects and users

    */

    @Override
    public List<Project> getAllProjectsByUserId(Long userId) throws AppEntityNotFoundException {

        User user = userService.getUserById(userId);
        log.info("Fetching all projects of user of ID: {} and username: {}..."
                , user.getUserId(), user.getUsername());

        List<Project> projects = projectRepository.findProjectsOfUserAndOrderByPositionAsc(user);
        log.info("All projects of user of ID: {} and username: {} were " +
                "successfully fetched.", user.getUserId(), user.getUsername());

        return projects;
    }
}
