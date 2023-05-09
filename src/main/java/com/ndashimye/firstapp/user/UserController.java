package com.ndashimye.firstapp.user;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.error.InvalidTimeFormatException;
import com.ndashimye.firstapp.userprofile.UserProfile;
import com.ndashimye.firstapp.usersettings.UserSettings;
import com.ndashimye.firstapp.todo.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/id/{userId}")
    public User getUserById(@PathVariable Long userId) throws AppEntityNotFoundException {
        return userService.getUserById(userId);
    }

    @GetMapping("/id/{userId}/todos")
    public List<Todo> getTodosByUserId(@PathVariable Long userId) throws AppEntityNotFoundException {
        return userService.getAllTodosByUserId(userId);
    }

    @GetMapping("/id/{userId}/todos/order-by/due-time/most-recent")
    public List<Todo> getTodosByUserIdOrderedByMostRecent(@PathVariable Long userId)
            throws AppEntityNotFoundException {
        return userService.getAllTodosByUserIdOrderedByMostRecent(userId);
    }

    @GetMapping("/id/{userId}/todos/order-by/due-time/least-recent")
    public List<Todo> getTodosByUserIdOrderedByLeastRecent(@PathVariable Long userId)
            throws AppEntityNotFoundException {
        return userService.getAllTodosByUserIdOrderedByLeastRecent(userId);
    }

    @GetMapping("/id/{userId}/todos/order-by/priority")
    public List<Todo> getTodosByUserIdOrderedByPriority(@PathVariable Long userId)
            throws AppEntityNotFoundException {
        return userService.getAllTodosByUserIdOrderedByPriority(userId);
    }

    @GetMapping("/id/{userId}/todos/between")
    public List<Todo> getTodosBetweenDates
            (@PathVariable Long userId, @RequestParam String start, @RequestParam String end)
            throws AppEntityNotFoundException, InvalidTimeFormatException {

            return userService.getAllTodosBetweenDates(userId, start, end);

    }

    @GetMapping("/email/{emailAddress}")
    public User getUserByEmailAddress(@PathVariable String emailAddress)
            throws AppEntityNotFoundException {

        return userService.getUserByEmailAddress(emailAddress);
    }


    @GetMapping("/username/{username}")
    public User getUserByUsername(@PathVariable String username) throws AppEntityNotFoundException {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/count")
    public Integer getUsersCount() {
        return userService.getUsersCount();
    }


    @PostMapping()
    public void addUser(@RequestBody User user) {
        userService.addNewUser(user);
    }

    @PutMapping("/{userId}")
    public void updateUser(@RequestBody User updatedUser,
                             @PathVariable Long userId) throws AppEntityNotFoundException {

        userService.updateUser(updatedUser, userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId)
            throws AppEntityNotFoundException {
        userService.deleteUser(userId);
    }

    @PutMapping("/{userId}/profile")
    public void updateUserProfile(@PathVariable Long userId,
                                    @RequestBody UserProfile updatedUserProfile)
            throws AppEntityNotFoundException {

        userService.updateUserProfile(userId, updatedUserProfile);
    }

    @PutMapping("/{userId}/settings")
    public void updateUserSettings(@PathVariable Long userId,
                                    @RequestBody UserSettings updatedUserSettings)
            throws AppEntityNotFoundException{

        userService.updateUserSettings(userId, updatedUserSettings);
    }
}
