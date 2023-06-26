package com.ndashimye.firstapp.blacklisteduser;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.goal.GoalDTO;

import java.util.List;

public interface BlackListedUserService {

    /*
    Service methods that handle all the operations
    related to the relationship between users and their restricted goals
    */
    List<GoalDTO> getRestrictedGoalsOfUserInProject(Long userId, Long projectId)
            throws AppEntityNotFoundException;

    void restrictUserFromAccessingGoalInProject(Long userId, Long projectId, Long goalId)
            throws AppEntityNotFoundException;

    void unrestrictUserFromAccessingGoalInProject(Long userId, Long projectId, Long goalId)
            throws AppEntityNotFoundException;
}
