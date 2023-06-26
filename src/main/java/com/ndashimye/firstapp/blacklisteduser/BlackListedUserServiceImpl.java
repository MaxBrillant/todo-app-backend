package com.ndashimye.firstapp.blacklisteduser;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.goal.*;
import com.ndashimye.firstapp.userproject.UserProject;
import com.ndashimye.firstapp.userproject.UserProjectService;
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
public class BlackListedUserServiceImpl implements BlackListedUserService {

    private final UserProjectService userProjectService;
    private final GoalService goalService;
    private final GoalDTOMapper goalDTOMapper;
    private GoalRepository goalRepository;
    private BlacklistedUserRepository blacklistedUserRepository;



    /*

    Service methods that handle all the operations
    related to the relationship between users and their restricted goals

    */

    @Override
    public List<GoalDTO> getRestrictedGoalsOfUserInProject(Long userId, Long projectId)
            throws AppEntityNotFoundException {

        UserProject userProject = userProjectService
                .getUserProjectByUserIdAndProjectId(userId, projectId);

        log.info("Fetching all blacklisted goals of user of ID: {} and username: {} in project of ID: {}..."
                , userProject.getUser().getUserId(), userProject.getUser().getUsername()
                , userProject.getProject().getProjectId());

        List<Goal> blackListedGoals = goalRepository
                .findBlacklistedGoalsOfUserAndOrderByPositionAsc(userProject);

        log.info("All blacklisted goals of user of ID: {} and username: {} in project of ID: {}" +
                        " were successfully fetched."
                , userProject.getUser().getUserId(), userProject.getUser().getUsername()
                , userProject.getProject().getProjectId());

        return blackListedGoals.stream()
                .map(goalDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public void restrictUserFromAccessingGoalInProject(Long userId, Long projectId, Long goalId)
            throws AppEntityNotFoundException {

        UserProject userProject = userProjectService
                .getUserProjectByUserIdAndProjectId(userId, projectId);

        Goal goal = goalService.getGoalById(goalId);
        log.info("Restricting user of ID: {} and username: {} from accessing goal of ID: {}..."
                , userProject.getUser().getUserId(), userProject.getUser().getUsername(), goal.getGoalId());

        blacklistedUserRepository.save(BlacklistedUser.builder()
                .userProject(userProject)
                .goal(goal)
                .build());

        log.info("User of ID: {} and username: {} was successfully restricted from accessing goal of ID: {}."
                , userProject.getUser().getUserId(), userProject.getUser().getUsername(), goal.getGoalId());
    }

    @Override
    public void unrestrictUserFromAccessingGoalInProject(Long userId, Long projectId, Long goalId)
            throws AppEntityNotFoundException {

        UserProject userProject = userProjectService
                .getUserProjectByUserIdAndProjectId(userId, projectId);

        Goal goal = goalService.getGoalById(goalId);
        log.info("Unrestricting user of ID: {} and username: {} from accessing goal of ID: {}..."
                , userProject.getUser().getUserId(), userProject.getUser().getUsername(), goal.getGoalId());

        BlacklistedUser blacklistedUser =
                blacklistedUserRepository.findByUserProjectAndGoal(userProject, goal)
                        .orElseThrow(()-> new AppEntityNotFoundException(BlacklistedUser.class));

        blacklistedUserRepository.delete(blacklistedUser);

        log.info("User of ID: {} and username: {} was successfully unrestricted from accessing goal of ID: {}."
                , userProject.getUser().getUserId(), userProject.getUser().getUsername(), goal.getGoalId());
    }
}
