package com.ndashimye.firstapp.user;

import com.ndashimye.firstapp.error.InvalidTimeFormatException;
import com.ndashimye.firstapp.userprofile.UserProfile;
import com.ndashimye.firstapp.usersettings.UserSettings;
import com.ndashimye.firstapp.usersettings.UserSettingsNotFoundException;
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
    public User getUserById(@PathVariable Long userId) throws UserNotFoundException {
        return userService.getUserById(userId);
    }

    @GetMapping("/id/{userId}/todos")
    public List<Todo> getTodosByUserId(@PathVariable Long userId) throws UserNotFoundException {
        return userService.getAllTodosByUserId(userId);
    }

    @GetMapping("/id/{userId}/todos/order-by/due-time/most-recent")
    public List<Todo> getTodosByUserIdOrderedByMostRecent(@PathVariable Long userId)
            throws UserNotFoundException {
        return userService.getAllTodosByUserIdOrderedByMostRecent(userId);
    }

    @GetMapping("/id/{userId}/todos/order-by/due-time/least-recent")
    public List<Todo> getTodosByUserIdOrderedByLeastRecent(@PathVariable Long userId)
            throws UserNotFoundException {
        return userService.getAllTodosByUserIdOrderedByLeastRecent(userId);
    }

    @GetMapping("/id/{userId}/todos/order-by/priority")
    public List<Todo> getTodosByUserIdOrderedByPriority(@PathVariable Long userId)
            throws UserNotFoundException {
        return userService.getAllTodosByUserIdOrderedByPriority(userId);
    }

    @GetMapping("/id/{userId}/todos/between")
    public List<Todo> getTodosBetweenDates
            (@PathVariable Long userId, @RequestParam String start, @RequestParam String end)
            throws UserNotFoundException, UserSettingsNotFoundException, InvalidTimeFormatException {

            return userService.getAllTodosBetweenDates(userId, start, end);

    }

    @GetMapping("/email/{emailAddress}")
    public User getUserByEmailAddress(@PathVariable String emailAddress)
            throws UserNotFoundException {

        return userService.getUserByEmailAddress(emailAddress);
    }


    @GetMapping("/username/{username}")
    public User getUserByUsername(@PathVariable String username) throws UserNotFoundException {
        return userService.getUserByUsername(username);
    }

    @GetMapping("/count")
    public Integer getUsersCount() {
        return userService.getUsersCount();
    }

    @GetMapping("/check/id/{userId}")
    public boolean checkUserExistence(@PathVariable Long userId){
        return userService.userIdExists(userId);
    }

    @GetMapping("/check/username/{username}")
    public boolean checkUsernameExistence(@PathVariable String username){
        return userService.usernameExists(username);
    }


    @GetMapping("/check/email/{emailAddress}")
    public boolean checkEmailExistence(@PathVariable String emailAddress){
        return userService.emailAddressExists(emailAddress);
    }


    @GetMapping("/check/password/{userId}/{password}")
    public boolean checkPassword(@PathVariable Long userId,
                                 @PathVariable String password)
            throws Exception {

        return userService.checkPassword(userId, password);
    }


    @PostMapping()
    public String addUser(@RequestBody User user) {

        userService.addNewUser(user);

        return "user "+user.getUsername()+" was added successfully";
    }

    @PutMapping("/{userId}")
    public String updateUser(@RequestBody User updatedUser,
                             @PathVariable Long userId)
            throws UserNotFoundException {

        User user = userService.getUserById(userId);

        userService.updateUser(updatedUser, user);

        return "user "+ userService.getUserById(userId).getUsername()+" was updated successfully";
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Long userId) throws UserNotFoundException {

        User user = userService.getUserById(userId);
        String username = user.getUsername();
        userService.deleteUser(user);
        
        return "user "+username+" was successfully deleted from the database";
    }




    @PostMapping("/{userId}/profile")
    public String addUserProfile(@PathVariable Long userId, @RequestBody UserProfile userProfile)
            throws UserNotFoundException {

        userService.addNewUserProfile(userId, userProfile);

        return "profile of profile id "+userProfile.getUserProfileId()+" was added successfully";
    }

    @PutMapping("/{userId}/profile")
    public String updateUserProfile(@PathVariable Long userId,
                                    @RequestBody UserProfile updatedUserProfile)
            throws UserNotFoundException {

        userService.updateUserProfile(userId, updatedUserProfile);

        return "profile of profile id "+ updatedUserProfile.getUserProfileId()+" was updated successfully";
    }

    @DeleteMapping("/{userId}/profile")
    public String deleteUserProfile(@PathVariable Long userId) throws UserNotFoundException {

        User user = userService.getUserById(userId);
        Long id = user.getProfile().getUserProfileId();
        userService.deleteUserProfile(userId);

        return "profile of profile id "+id+" was successfully deleted from the database";
    }




    @PostMapping("/{userId}/settings")
    public String addUserSettings(@PathVariable Long userId, @RequestBody UserSettings userSettings)
            throws UserNotFoundException {

        userService.addNewUserSettings(userId, userSettings);

        return "settings of settings id "+userSettings.getUserSettingsId()+" were added successfully";
    }

    @PutMapping("/{userId}/settings")
    public String updateUserSettings(@PathVariable Long userId,
                                    @RequestBody UserSettings updatedUserSettings)
            throws UserNotFoundException {

        userService.updateUserSettings(userId, updatedUserSettings);

        return "settings of settings id "+ updatedUserSettings.getUserSettingsId()+" were updated successfully";
    }

    @DeleteMapping("/{userId}/settings")
    public String deleteUserSettings(@PathVariable Long userId) throws UserNotFoundException {

        User user = userService.getUserById(userId);
        Long id = user.getSettings().getUserSettingsId();
        userService.deleteUserSettings(userId);

        return "settings of settings id "+id+" were successfully deleted from the database";
    }
}
