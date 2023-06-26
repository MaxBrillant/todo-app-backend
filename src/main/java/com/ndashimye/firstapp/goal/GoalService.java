package com.ndashimye.firstapp.goal;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.error.InvalidTimeFormatException;
import com.ndashimye.firstapp.project.Project;
import java.util.List;

public interface GoalService {

    //Service methods that handle all the operations related to goals
    GoalDTO getGoalDTOById(Long goalId) throws AppEntityNotFoundException;

    Goal getGoalById(Long goalId) throws AppEntityNotFoundException;

    void updateGoal(GoalCreationDTO updatedGoal, Long goalId)
            throws AppEntityNotFoundException;

    void deleteGoal(Long goalId)
            throws AppEntityNotFoundException;

    /*
    Service methods that handle all the operations
    related to the relationship between goals and projects
    */
    List<GoalDTO> getAllGoalsOfUserByProjectId(Long userId, Long projectId)
            throws AppEntityNotFoundException;

    List<GoalDTO> getLastGoalsOfUserByProjectId(Long userId, Long projectId)
            throws AppEntityNotFoundException;

    void addNewGoalToProject(GoalCreationDTO goalCreationDTO, Long projectId)
            throws AppEntityNotFoundException;


    void moveGoalToProject(Long Id, Long projectId)
            throws AppEntityNotFoundException;

    void assignPositionInProjectToNewGoal(Project project, Goal goal);

    void updateGoalPositionInProject(Long goalId, int newPosition)
            throws AppEntityNotFoundException;

    /*
    Service methods that handle all the operations
    related to the relationship between goals and users
    */
    List<GoalDTO> getAllGoalsByUserId(Long userId) throws AppEntityNotFoundException;

    List<GoalDTO> getAllGoalsByUserIdOrderedByMostRecent(Long userId)
            throws AppEntityNotFoundException;

    List<GoalDTO> getAllGoalsByUserIdOrderedByLeastRecent(Long userId)
            throws AppEntityNotFoundException;

    List<GoalDTO> getAllGoalsByUserIdBetweenDates
            (Long userId, String start, String end)
            throws AppEntityNotFoundException, InvalidTimeFormatException;
}
