package com.ndashimye.firstapp.userproject;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.project.Project;
import com.ndashimye.firstapp.project.ProjectCreationDTO;
import com.ndashimye.firstapp.project.ProjectRepository;
import com.ndashimye.firstapp.project.ProjectService;
import com.ndashimye.firstapp.user.User;
import com.ndashimye.firstapp.user.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class UserProjectServiceImpl implements UserProjectService {

    private final UserService userService;
    private final ProjectService projectService;
    private final UserProjectDTOMapper userProjectDTOMapper;
    private UserProjectRepository userProjectRepository;
    private ProjectRepository projectRepository;



    /*

    Service methods that handle all the operations
    related to the relationship between users and their projects

    */

    @Override
    public UserProject getUserProjectByUserIdAndProjectId
            (Long userId, Long projectId)
            throws AppEntityNotFoundException {

        User user = userService.getUserById(userId);
        Project project = projectService.getProjectById(projectId);

        log.info("Fetching information about the relationship between " +
                "the user of ID: {} and the project of ID: {}...", userId, projectId);
        UserProject userProject = userProjectRepository.findByUserAndProject(user, project)
                .orElseThrow(() -> new AppEntityNotFoundException(UserProject.class));
        log.info("Information about the relationship between " +
                "the user of ID: {} and the project of ID: {} was successfully fetched.", userId, projectId);

        return userProject;
    }

    @Override
    public List<UserProjectDTO> getAllUserProjects(Long userId) throws AppEntityNotFoundException {

        User user = userService.getUserById(userId);
        log.info("Fetching all projects of user of ID: {} and username: {}..."
                , user.getUserId(), user.getUsername());

        List<UserProject> userProjects = userProjectRepository
                .findUserProjectsOfUserAndOrderByPositionAsc(user);
        log.info("All projects of user of ID: {} and username: {} were " +
                "successfully fetched.", user.getUserId(), user.getUsername());

        return userProjects.stream()
                .map(userProjectDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public void addNewProjectToUser(Long userId, ProjectCreationDTO projectCreationDTO)
            throws AppEntityNotFoundException {

        User user = userService.getUserById(userId);
        log.info("Adding a new project...");

        Project project = Project
                .builder()
                .name(projectCreationDTO.name())
                .description(projectCreationDTO.description())
                .coverImageUrl(projectCreationDTO.coverURL())
                .build();

        projectRepository.save(project);

        assignPositionToNewProject(UserProject.builder()
                .user(user)
                .project(project)
                .projectRole(ProjectRole.CREATOR)
                .build());
        log.info("Project of ID: {} was successfully added.", project.getProjectId());
    }

    @Override
    public void addExistingProjectToUser(Long userId, Long projectId)
            throws AppEntityNotFoundException {

        User user = userService.getUserById(userId);
        Project project = projectService.getProjectById(projectId);

        log.info("Adding a project of ID: {} to user of ID: {}..."
                , project.getProjectId(), user.getUserId());

        assignPositionToNewProject(UserProject.builder()
                .user(user)
                .project(project)
                .projectRole(ProjectRole.CONTRIBUTOR)
                .build());
        projectRepository.save(project);
        log.info("Project of ID: {} was successfully added.", project.getProjectId());
    }

    @Override
    public void updateProject(long userId, long projectId, ProjectCreationDTO updatedProject)
            throws AppEntityNotFoundException {

        UserProject userProject = getUserProjectByUserIdAndProjectId(userId, projectId);

        if (!userProject.getProjectRole().equals(ProjectRole.CONTRIBUTOR)) {
            log.info("Updating project of ID: {}...", userProject.getProject().getProjectId());

            userProject.getProject().setName(updatedProject.name());
            userProject.getProject().setDescription(updatedProject.description());
            userProject.getProject().setCoverImageUrl(updatedProject.coverURL());

            //projectRepository.save(project);
            log.info("Project of ID: {} was successfully updated.", userProject.getProject().getProjectId());
        }
    }

    @Override
    public void deleteProject(long userId, long projectId)
            throws AppEntityNotFoundException {

        UserProject userProject = getUserProjectByUserIdAndProjectId(userId, projectId);

        if (!userProject.getProjectRole().equals(ProjectRole.CONTRIBUTOR)) {
            log.info("Deleting project of ID: {}...", userProject.getProject().getProjectId());
            Project deletedProject = userProject.getProject();
            projectRepository.delete(userProject.getProject());
            log.info("Project of ID: {} was successfully deleted.", deletedProject.getProjectId());
        }
    }

    @Override
    public void updateUserProjectRole(long userId, long projectId, String role)
            throws AppEntityNotFoundException {

        if (role.equals("CONTRIBUTOR") || role.equals("ADMIN")) {

            UserProject userProject = getUserProjectByUserIdAndProjectId(userId, projectId);
            if (role.equals("ADMIN")) {
                userProject.setProjectRole(ProjectRole.ADMIN);
            }else {
                userProject.setProjectRole(ProjectRole.CONTRIBUTOR);
            }
        }
    }

    @Override
    public void assignPositionToNewProject(UserProject userProject) {

        log.info("Calculating the maximum position value of all existing projects...");
        // Get the maximum position value from all existing projects
        Integer maxPosition = userProjectRepository.getMaxPosition(userProject.getUser());

        // If there are no existing projects, set the position to 1
        if (maxPosition == null) {
            maxPosition = 0;
        }

        log.info("Assigning a position to a new project of ID: {}..."
                , userProject.getProject().getProjectId());
        // Assign the new peoject's position to be the maximum position + 1
        userProject.setPosition(maxPosition + 1);

        // Save the new user project
        userProjectRepository.save(userProject);
        log.info("Position of value: {} was successfully assigned to the new project of ID: {}."
                , userProject.getPosition(), userProject.getProject().getProjectId());
    }

    @Override
    public void updateProjectPosition(Long userId, Long projectId, int newPosition)
            throws AppEntityNotFoundException {

        UserProject userProject = getUserProjectByUserIdAndProjectId(userId, projectId);

        log.info("Getting the current position of project of ID: {}..."
                , userProject.getProject().getProjectId());
        // Get the current position of the project
        int currentPosition = userProject.getPosition();

        // If the new position is equal to the current position, do nothing
        if (newPosition == currentPosition) {
            return;
        }

        log.info("Getting all projects with positions between the current ({}) and new ({}) positions..."
                , currentPosition, newPosition);

        // Get the projects with positions between the current and new positions
        List<UserProject> projectsToUpdate;
        if (newPosition > currentPosition) {
            projectsToUpdate = userProjectRepository.findProjectsWithPositionsBetween
                    (userProject.getUser(), currentPosition + 1, newPosition);
        } else {
            projectsToUpdate = userProjectRepository.findProjectsWithPositionsBetween
                    (userProject.getUser(), newPosition, currentPosition - 1);
        }

        log.info("Updating positions of all the projects that are between position {} and {}..."
                , currentPosition, newPosition);
        // Update the positions of the affected projects
        for (UserProject projectToUpdate : projectsToUpdate) {
            if (newPosition > currentPosition) {
                projectToUpdate.setPosition(projectToUpdate.getPosition() - 1);
            } else {
                projectToUpdate.setPosition(projectToUpdate.getPosition() + 1);
            }
            userProjectRepository.save(projectToUpdate);
        }
        log.info("The positions of all projects that are between position {} and {} were successfully updated."
                , currentPosition, newPosition);

        log.info("Updating the position of project of ID: {}...", projectId);
        // Update the position of the target project
        userProject.setPosition(newPosition);
        userProjectRepository.save(userProject);
        log.info("The position of project of ID: {} was successfully updated.", projectId);
    }
}
