package com.ndashimye.firstapp.user;

import com.ndashimye.firstapp.blacklisteduser.BlackListedUserService;
import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.error.InvalidTimeFormatException;
import com.ndashimye.firstapp.goal.GoalService;
import com.ndashimye.firstapp.task.TaskService;
import com.ndashimye.firstapp.goal.GoalDTO;
import com.ndashimye.firstapp.userproject.UserProjectDTO;
import com.ndashimye.firstapp.userproject.UserProjectService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final GoalService goalService;
    private final TaskService taskService;
    private final UserProjectService userProjectService;
    private final BlackListedUserService blackListedUserService;



    //HTTP endpoints that handle all the operations related to users

    @PostMapping()
    public void addUser(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        userService.addNewUser(userRegistrationDTO);
    }

    @PutMapping("/{userId}")
    public void updateUser(@RequestBody UserRegistrationDTO updatedUser,
                           @PathVariable Long userId)
            throws AppEntityNotFoundException {

        userService.updateUser(updatedUser, userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId)
            throws AppEntityNotFoundException {
        userService.deleteUser(userId);
    }


    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDTO getUserById(@PathVariable Long userId)
            throws AppEntityNotFoundException {

        return userService.getUserDTOById(userId);
    }

    @GetMapping("/email/{emailAddress}")
    public UserDTO getUserByEmailAddress(@PathVariable String emailAddress)
            throws AppEntityNotFoundException {

        return userService.getUserByEmailAddress(emailAddress);
    }

    @GetMapping("/username/{username}")
    public UserDTO getUserByUsername(@PathVariable String username)
            throws AppEntityNotFoundException {

        return userService.getUserByUsername(username);
    }



    /*

    HTTP endpoints that handle all the operations
    related to the relationship between users and projects

    */

    @GetMapping("/{userId}/projects/{projectId}/goals")
    public List<GoalDTO> getGoalsOfUserByProjectId(@PathVariable Long userId,
                                                   @PathVariable Long projectId)
            throws AppEntityNotFoundException {

        return goalService.getAllGoalsOfUserByProjectId(userId, projectId);
    }

    @GetMapping("/{userId}/projects")
    public List<UserProjectDTO> getProjectsByUserId(@PathVariable Long userId)
            throws AppEntityNotFoundException {

        return userProjectService.getAllUserProjects(userId);
    }



    /*

    HTTP endpoints that handle all the operations
    related to the relationship between users and goals

    */

    @GetMapping("/{userId}/goals")
    public List<GoalDTO> getGoalsByUserId(@PathVariable Long userId)
            throws AppEntityNotFoundException {

        return goalService.getAllGoalsByUserId(userId);
    }

    @GetMapping("/{userId}/goals/order-by/due-time/most-recent")
    public List<GoalDTO> getGoalsByUserIdOrderedByMostRecent(@PathVariable Long userId)
            throws AppEntityNotFoundException {

        return goalService.getAllGoalsByUserIdOrderedByMostRecent(userId);
    }

    @GetMapping("/{userId}/goals/order-by/due-time/least-recent")
    public List<GoalDTO> getGoalsByUserIdOrderedByLeastRecent(@PathVariable Long userId)
            throws AppEntityNotFoundException {

        return goalService.getAllGoalsByUserIdOrderedByLeastRecent(userId);
    }

    @GetMapping("/{userId}/goals/between")
    public List<GoalDTO> getGoalsBetweenDates
            (@PathVariable Long userId
                    , @RequestParam String start, @RequestParam String end)
            throws AppEntityNotFoundException, InvalidTimeFormatException {

            return goalService.getAllGoalsByUserIdBetweenDates(userId, start, end);

    }



    /*

    HTTP endpoints that handle all the operations
    related to the relationship between users and restricted goals

    */

    @GetMapping("/{userId}/projects/{projectId}/restricted-goals")
    public List<GoalDTO> getRestrictedGoalsOfUserInProject
            (@PathVariable Long userId, @PathVariable Long projectId)
            throws AppEntityNotFoundException {

        return blackListedUserService.getRestrictedGoalsOfUserInProject(userId, projectId);
    }


    @PutMapping("/{userId}/projects/{projectId}/goals/{goalId}/restrict-access")
    public void restrictUserFromAccessingGoalInProject
            (@PathVariable Long userId, @PathVariable Long projectId
                    , @PathVariable Long goalId)
            throws AppEntityNotFoundException {

        blackListedUserService.restrictUserFromAccessingGoalInProject(userId, projectId, goalId);
    }

    @PutMapping("/{userId}/projects/{projectId}/goals/{goalId}/unrestrict")
    public void unrestrictUserFromAccessingTodoInProject
            (@PathVariable Long userId, @PathVariable Long projectId
                    , @PathVariable Long goalId)
            throws AppEntityNotFoundException {

        blackListedUserService.unrestrictUserFromAccessingGoalInProject(userId, projectId, goalId);
    }


    @PutMapping("/{userId}/tasks/{taskId}/complete")
    public void completeTask(@PathVariable Long taskId, @PathVariable Long userId)
            throws AppEntityNotFoundException {

        taskService.completeTask(taskId, userId);
    }
}
