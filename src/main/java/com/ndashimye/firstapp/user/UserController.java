package com.ndashimye.firstapp.user;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.error.InvalidTimeFormatException;
import com.ndashimye.firstapp.task.TaskDTO;
import com.ndashimye.firstapp.task.TaskService;
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
    private final TaskService taskService;
    private final UserProjectService userProjectService;



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

    @GetMapping("/{userId}/projects")
    public List<UserProjectDTO> getProjectsByUserId(@PathVariable Long userId)
            throws AppEntityNotFoundException {

        return userProjectService.getAllUserProjects(userId);
    }



    /*

    HTTP endpoints that handle all the operations
    related to the relationship between users and tasks

    */

    @GetMapping("/{userId}/tasks")
    public List<TaskDTO> getTasksByUserId(@PathVariable Long userId)
            throws AppEntityNotFoundException {

        return taskService.getAllTasksByUserId(userId);
    }

    @GetMapping("/{userId}/tasks/order-by/due-time/most-recent")
    public List<TaskDTO> getTasksByUserIdOrderedByMostRecent(@PathVariable Long userId)
            throws AppEntityNotFoundException {

        return taskService.getAllTasksByUserIdOrderedByMostRecent(userId);
    }

    @GetMapping("/{userId}/tasks/order-by/due-time/least-recent")
    public List<TaskDTO> getTasksByUserIdOrderedByLeastRecent(@PathVariable Long userId)
            throws AppEntityNotFoundException {

        return taskService.getAllTasksByUserIdOrderedByLeastRecent(userId);
    }

    @GetMapping("/{userId}/tasks/between")
    public List<TaskDTO> getTasksBetweenDates
            (@PathVariable Long userId
                    , @RequestParam String start, @RequestParam String end)
            throws AppEntityNotFoundException, InvalidTimeFormatException {

            return taskService.getAllTasksByUserIdBetweenDates(userId, start, end);

    }
}
