package com.ndashimye.firstapp.user;

import com.ndashimye.firstapp.blacklisteduser.BlackListedUserService;
import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.error.InvalidTimeFormatException;
import com.ndashimye.firstapp.project.Project;
import com.ndashimye.firstapp.project.ProjectService;
import com.ndashimye.firstapp.task.TaskService;
import com.ndashimye.firstapp.todo.Todo;
import com.ndashimye.firstapp.todo.TodoService;
import com.ndashimye.firstapp.userprofile.UserProfile;
import com.ndashimye.firstapp.usersettings.UserSettings;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final TodoService todoService;
    private final TaskService taskService;
    private final ProjectService projectService;
    private final BlackListedUserService blackListedUserService;



    //HTTP endpoints that handle all the operations related to users

    @PostMapping()
    public void addUser(@RequestBody User user) {
        userService.addNewUser(user);
    }

    @PutMapping("/{userId}")
    public void updateUser(@RequestBody User updatedUser,
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
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId)
            throws AppEntityNotFoundException {

        return userService.getUserById(userId);
    }

    @GetMapping("/email/{emailAddress}")
    public User getUserByEmailAddress(@PathVariable String emailAddress)
            throws AppEntityNotFoundException {

        return userService.getUserByEmailAddress(emailAddress);
    }

    @GetMapping("/username/{username}")
    public User getUserByUsername(@PathVariable String username)
            throws AppEntityNotFoundException {

        return userService.getUserByUsername(username);
    }



    /*

    HTTP endpoints that handle all the operations
    related to the relationship between users and their profiles/settings

    */

    @PutMapping("/{userId}/profile")
    public void updateUserProfile(@PathVariable Long userId,
                                  @RequestBody UserProfile updatedUserProfile)
            throws AppEntityNotFoundException {

        userService.updateUserProfile(userId, updatedUserProfile);
    }

    @PutMapping("/{userId}/settings")
    public void updateUserSettings(@PathVariable Long userId,
                                   @RequestBody UserSettings updatedUserSettings)
            throws AppEntityNotFoundException {

        userService.updateUserSettings(userId, updatedUserSettings);
    }



    /*

    HTTP endpoints that handle all the operations
    related to the relationship between users and projects

    */

    @GetMapping("/{userId}/projects/{projectId}/todos")
    public List<Todo> getTodosOfUserByProjectId(@PathVariable Long userId,
                                          @PathVariable Long projectId)
            throws AppEntityNotFoundException {

        return todoService.getAllTodosOfUserByProjectId(userId, projectId);
    }

    @GetMapping("/{userId}/projects")
    public List<Project> getProjectsByUserId(@PathVariable Long userId)
            throws AppEntityNotFoundException {

        return projectService.getAllProjectsByUserId(userId);
    }



    /*

    HTTP endpoints that handle all the operations
    related to the relationship between users and todos

    */

    @GetMapping("/{userId}/todos")
    public List<Todo> getTodosByUserId(@PathVariable Long userId)
            throws AppEntityNotFoundException {

        return todoService.getAllTodosByUserId(userId);
    }

    @GetMapping("/{userId}/todos/order-by/priority")
    public List<Todo> getTodosByUserIdOrderedByPriority(@PathVariable Long userId)
            throws AppEntityNotFoundException {

        return todoService.getAllTodosByUserIdOrderedByPriority(userId);
    }

    @GetMapping("/{userId}/todos/order-by/due-time/most-recent")
    public List<Todo> getTodosByUserIdOrderedByMostRecent(@PathVariable Long userId)
            throws AppEntityNotFoundException {

        return todoService.getAllTodosByUserIdOrderedByMostRecent(userId);
    }

    @GetMapping("/{userId}/todos/order-by/due-time/least-recent")
    public List<Todo> getTodosByUserIdOrderedByLeastRecent(@PathVariable Long userId)
            throws AppEntityNotFoundException {

        return todoService.getAllTodosByUserIdOrderedByLeastRecent(userId);
    }

    @GetMapping("/{userId}/todos/between")
    public List<Todo> getTodosBetweenDates
            (@PathVariable Long userId
                    , @RequestParam String start, @RequestParam String end)
            throws AppEntityNotFoundException, InvalidTimeFormatException {

            return todoService.getAllTodosByUserIdBetweenDates(userId, start, end);

    }



    /*

    HTTP endpoints that handle all the operations
    related to the relationship between users and restricted todos

    */

    @GetMapping("/{userId}/projects/{projectId}/restricted-todos")
    public List<Todo> getRestrictedTodosOfUserInProject
            (@PathVariable Long userId, @PathVariable Long projectId)
            throws AppEntityNotFoundException {

        return blackListedUserService.getRestrictedTodosOfUserInProject(userId, projectId);
    }


    @PutMapping("/{userId}/projects/{projectId}/todos/{todoId}/restrict-access")
    public void restrictUserFromAccessingTodoInProject
            (@PathVariable Long userId, @PathVariable Long projectId
                    , @PathVariable Long todoId)
            throws AppEntityNotFoundException {

        blackListedUserService.restrictUserFromAccessingTodoInProject(userId, projectId, todoId);
    }


    @PutMapping("/{userId}/tasks/{taskId}/complete")
    public void completeTask(@PathVariable Long taskId, @PathVariable Long userId)
            throws AppEntityNotFoundException {

        taskService.completeTask(taskId, userId);
    }
}
