package com.ndashimye.firstapp.goal;

import com.ndashimye.firstapp.ZonedDateTimeAttributeConverter;
import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.error.InvalidTimeFormatException;
import com.ndashimye.firstapp.project.Project;
import com.ndashimye.firstapp.project.ProjectService;
import com.ndashimye.firstapp.user.User;
import com.ndashimye.firstapp.user.UserService;
import com.ndashimye.firstapp.usersettings.UserSettings;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final UserService userService;
    private final ProjectService projectService;
    private final GoalDTOMapper goalDTOMapper;
    private GoalRepository goalRepository;



    //Service methods that handle all the operations related to goals

    @Override
    public GoalDTO getGoalDTOById(Long goalId) throws AppEntityNotFoundException {
        log.info("Fetching goal by ID: {}...", goalId);
        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new AppEntityNotFoundException(Goal.class));
        log.info("Goal of ID: {} was successfully fetched.", goalId);

        return goalDTOMapper.apply(goal);
    }

    @Override
    public Goal getGoalById(Long goalId) throws AppEntityNotFoundException {
        return goalRepository.findById(goalId)
                .orElseThrow(() -> new AppEntityNotFoundException(Goal.class));
    }

    @Override
    public void updateGoal(GoalCreationDTO updatedGoal, Long goalId)
            throws AppEntityNotFoundException {

        Goal goal = getGoalById(goalId);
        log.info("Updating goal of ID: {}...", goal.getGoalId());

        goal.setName(updatedGoal.name());
        goal.setDescription(updatedGoal.description());
        goal.setDueTime(updatedGoal.dueTime());

        goalRepository.save(goal);
        log.info("Goal of ID: {} was successfully updated.", goal.getGoalId());
    }

    @Override
    public void deleteGoal(Long goalId)
            throws AppEntityNotFoundException {

        Goal goal = getGoalById(goalId);
        log.info("Deleting goal of ID: {}...", goal.getGoalId());

        Goal deletedGoal = goal;
        goalRepository.delete(goal);
        log.info("Goal of ID: {} was successfully deleted.", deletedGoal.getGoalId());
    }



    /*

    Service methods that handle all the operations
    related to the relationship between goals and projects

    */

    @Override
    public List<GoalDTO> getAllGoalsOfUserByProjectId(Long userId, Long projectId)
            throws AppEntityNotFoundException {

        User user = userService.getUserById(userId);
        Project project = projectService.getProjectById(projectId);
        log.info("Fetching all goals of project of ID: {}...", projectId);
        List<Goal> goals = goalRepository.findAccessibleGoalsByProjectAndUserAndOrderByPositionAsc(user, project);
        log.info("All goals of project of ID: {} were successfully fetched.", project.getProjectId());

        return goals.stream()
                .map(goalDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<GoalDTO> getLastGoalsOfUserByProjectId(Long userId, Long projectId)
            throws AppEntityNotFoundException {

        User user = userService.getUserById(userId);
        Project project = projectService.getProjectById(projectId);
        log.info("Fetching the last goals of project of ID: {}...", projectId);
        List<Goal> goals = goalRepository.findAccessibleGoalsByProjectAndUserAndOrderByPositionDesc(user, project);
        log.info("The last goals of project of ID: {} were successfully fetched.", project.getProjectId());

        return goals.stream()
                .map(goalDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public void addNewGoalToProject(GoalCreationDTO goalCreationDTO, Long projectId)
            throws AppEntityNotFoundException {

        log.info("Adding a new goal to project of ID: {}...", projectId);

        //Checking if the project exists and assigning it to the new goal
        Project project = projectService.getProjectById(projectId);
        Goal goal = Goal.builder()
                .name(goalCreationDTO.name())
                .description(goalCreationDTO.description())
                .dueTime(goalCreationDTO.dueTime())
                .build();

        goal.setProject(project);

        //Assign position to new goal
        assignPositionInProjectToNewGoal(project, goal);

        goalRepository.save(goal);
        log.info("Goal of ID: {} was successfully added to project of ID: {}."
                , goal.getGoalId(), project.getProjectId());
    }

    @Override
    public void moveGoalToProject(Long goalId, Long projectId)
            throws AppEntityNotFoundException {

        Goal goal = getGoalById(goalId);
        Project project = projectService.getProjectById(projectId);
        log.info("Moving goal of ID: {} to project of ID: {}..."
                , goal.getGoalId(), project.getProjectId());

        goal.setProject(project);
        goalRepository.save(goal);
        log.info("Goal of ID: {} was successfully moved to project of ID: {}."
                , goal.getGoalId(), project.getProjectId());
    }

    @Override
    public void assignPositionInProjectToNewGoal(Project project, Goal goal) {

        log.info("Calculating the maximum position value of all existing goals...");
        // Get the maximum position value from all existing goals
        Integer maxPosition = goalRepository.getMaxPosition(project);

        // If there are no existing goals, set the position to 1
        if (maxPosition == null) {
            maxPosition = 0;
        }

        log.info("Assigning a position to a new goal of ID: {}...", goal.getGoalId());
        // Assign the new goal's position to be the maximum position + 1
        goal.setPosition(maxPosition + 1);

        // Save the new goal
        goalRepository.save(goal);
        log.info("Position of value: {} was successfully assigned to the new goal of ID: {}."
                , goal.getPosition(), goal.getGoalId());
    }

    @Override
    public void updateGoalPositionInProject(Long goalId, int newPosition)
            throws AppEntityNotFoundException {

        Goal goal = getGoalById(goalId);

        log.info("Getting the current position of goal of ID: {}...", goal.getGoalId());
        // Get the current position of the goal
        int currentPosition = goal.getPosition();

        // If the new position is equal to the current position, do nothing
        if (newPosition == currentPosition) {
            return;
        }

        log.info("Getting all goals with positions between the current ({}) and new ({}) positions..."
                , currentPosition, newPosition);

        // Get the goals with positions between the current and new positions
        List<Goal> goalsToUpdate;
        if (newPosition > currentPosition) {
            goalsToUpdate = goalRepository.findAccessibleGoalsOfUserWithPositionsBetween
                    (goal.getProject(), currentPosition + 1, newPosition);
        } else {
            goalsToUpdate = goalRepository.findAccessibleGoalsOfUserWithPositionsBetween
                    (goal.getProject(), newPosition, currentPosition - 1);
        }

        log.info("Updating positions of all the goals that are between position {} and {}...", currentPosition, newPosition);
        // Update the positions of the affected goals
        for (Goal goalToUpdate : goalsToUpdate) {
            if (newPosition > currentPosition) {
                goalToUpdate.setPosition(goalToUpdate.getPosition() - 1);
            } else {
                goalToUpdate.setPosition(goalToUpdate.getPosition() + 1);
            }
            goalRepository.save(goalToUpdate);
        }
        log.info("The positions of all goals that are between position {} and {} were successfully updated."
                , currentPosition, newPosition);

        log.info("Updating the position of goal of ID: {}...", goal.getGoalId());
        // Update the position of the target goal
        goal.setPosition(newPosition);
        goalRepository.save(goal);
        log.info("The position of goal of ID: {} was successfully updated.", goal.getGoalId());
    }



    /*

    Service methods that handle all the operations
    related to the relationship between goals and users

    */

    @Override
    public List<GoalDTO> getAllGoalsByUserId(Long userId)
            throws AppEntityNotFoundException {

        User user = userService.getUserById(userId);
        log.info("Fetching all goals of user of ID: {} and username: {}..."
                , user.getUserId(), user.getUsername());

        List<Goal> goals = goalRepository.findAccessibleGoalsOfUserAndOrderByPositionAsc(user);
        log.info("All goals of user of ID: {} and username: {} were " +
                "successfully fetched.", user.getUserId(), user.getUsername());

        return goals.stream()
                .map(goalDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<GoalDTO> getAllGoalsByUserIdOrderedByMostRecent(Long userId)
            throws AppEntityNotFoundException {

        User user = userService.getUserById(userId);
        log.info("Fetching all goals of user of ID: {} and username: {} ordered from most to least recent..."
                , user.getUserId(), user.getUsername());

        List<Goal> goals = goalRepository.findAccessibleGoalsOfUserAndOrderByDueTimeDesc(user);
        log.info("All goals of user of ID: {} and username: {} ordered from most to " +
                "least recent were successfully fetched.", user.getUserId(), user.getUsername());

        return goals.stream()
                .map(goalDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<GoalDTO> getAllGoalsByUserIdOrderedByLeastRecent(Long userId)
            throws AppEntityNotFoundException {

        User user = userService.getUserById(userId);
        log.info("Fetching all goals of user of ID: {} and username: {} ordered from least to most recent..."
                , user.getUserId(), user.getUsername());

        List<Goal> goals = goalRepository.findAccessibleGoalsOfUserAndOrderByDueTimeAsc(user);
        log.info("All goals of user of ID: {} and username: {} ordered from least to " +
                "most recent were successfully fetched.", user.getUserId(), user.getUsername());

        return goals.stream()
                .map(goalDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<GoalDTO> getAllGoalsByUserIdBetweenDates
            (Long userId, String start, String end)
            throws AppEntityNotFoundException, InvalidTimeFormatException {


        LocalDate startDate;
        LocalDate endDate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            startDate = LocalDate.parse(start, formatter);
            endDate = LocalDate.parse(end, formatter);
        } catch (Exception e) {
            throw new InvalidTimeFormatException();
        }

        User user = userService.getUserById(userId);

        log.info("Getting the timezone information from the user settings...");

        UserSettings userSettings = user.getSettings();

        ZoneId zoneId = ZoneId.of(userSettings.getTimeZone()); // or specify a specific timezone


        log.info("Successfully accessed the user's timezone information.");

        ZonedDateTime zonedStartDate = startDate.atStartOfDay(zoneId);
        ZonedDateTime zonedEndDate = endDate.plusDays(1).atStartOfDay(zoneId);


        log.info("Fetching all goals of user of ID: {} and username: {} between {} and {}..."
                , user.getUserId(), user.getUsername(), start, end);

        List<Goal> goals = goalRepository.findAccessibleGoalsOfUserAndDueTimeBetween(user.getUserId(),
                Timestamp.valueOf(ZonedDateTimeAttributeConverter.toUtcZoneId(zonedStartDate).toLocalDateTime()),
                Timestamp.valueOf(ZonedDateTimeAttributeConverter.toUtcZoneId(zonedEndDate).toLocalDateTime()));

        log.info("All goals of user of ID: {} and username: {} between {} and {} " +
                "were successfully fetched.", user.getUserId(), user.getUsername(), start, end);

        return goals.stream()
                .map(goalDTOMapper)
                .collect(Collectors.toList());
    }
}
