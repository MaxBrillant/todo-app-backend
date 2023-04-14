package com.ndashimye.firstapp.user;
import com.ndashimye.firstapp.error.InvalidTimeFormatException;
import com.ndashimye.firstapp.userprofile.UserProfile;
import com.ndashimye.firstapp.userprofile.UserProfileNotFoundException;
import com.ndashimye.firstapp.usersettings.UserSettings;
import com.ndashimye.firstapp.usersettings.UserSettingsNotFoundException;
import com.ndashimye.firstapp.task.Task;
import com.ndashimye.firstapp.todo.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    public User getUserById(@PathVariable Integer userId) throws UserNotFoundException {
        return userService.getUserById(userId);
    }

    @GetMapping("/id/{userId}/profile")
    public UserProfile getUserProfileByUserId(@PathVariable Integer userId) throws UserProfileNotFoundException, UserNotFoundException {
        return userService.getUserProfileByUserId(userId);
    }

    @GetMapping("/id/{userId}/settings")
    public UserSettings getUserSettingsByUserId(@PathVariable Integer userId) throws UserNotFoundException, UserSettingsNotFoundException {
        return userService.getUserSettingsByUserId(userId);
    }

    @GetMapping("/id/{userId}/todos")
    public List<Todo> getTodosByUserId(@PathVariable Integer userId) throws UserNotFoundException {
        return userService.getAllTodosByUserId(userId);
    }

    @GetMapping("/id/{userId}/todos/order-by/due-time/most-recent")
    public List<Todo> getTodosByUserIdOrderedByMostRecent(@PathVariable Integer userId) throws UserNotFoundException {
        return userService.getAllTodosByUserIdOrderedByMostRecent(userId);
    }

    @GetMapping("/id/{userId}/todos/order-by/due-time/least-recent")
    public List<Todo> getTodosByUserIdOrderedByLeastRecent(@PathVariable Integer userId) throws UserNotFoundException {
        return userService.getAllTodosByUserIdOrderedByLeastRecent(userId);
    }

    @GetMapping("/id/{userId}/todos/order-by/priority")
    public List<Todo> getTodosByUserIdOrderedByPriority(@PathVariable Integer userId) throws UserNotFoundException {
        return userService.getAllTodosByUserIdOrderedByPriority(userId);
    }

    @GetMapping("/id/{userId}/todos/between")
    public List<Todo> getTodosBetweenDates
            (@PathVariable Integer userId, @RequestParam String start, @RequestParam String end)
            throws InvalidTimeFormatException, UserNotFoundException, UserSettingsNotFoundException {

        LocalDate startDate;
        LocalDate endDate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            startDate = LocalDate.parse(start, formatter);
            endDate = LocalDate.parse(end, formatter);
        }
        catch (Exception e){
            throw new InvalidTimeFormatException();
        }
            return userService.getAllTodosBetweenDates(userId, startDate, endDate);

    }


    @GetMapping("/id/{userId}/today-tasks/on")
    public List<Task> getAllTodayTasksOnDate
            (@PathVariable Integer userId, @RequestParam String date)
            throws InvalidTimeFormatException, UserNotFoundException, UserSettingsNotFoundException {

        LocalDate startDate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            startDate = LocalDate.parse(date, formatter);
        }
        catch (Exception e){
            throw new InvalidTimeFormatException();
        }
        return userService.getAllTodayTasksOnDate(userId, startDate);

    }

    @GetMapping("/email/{emailAddress}")
    public User getUserByEmailAddress(@PathVariable String emailAddress) throws UserNotFoundException {
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
    public boolean checkUserExistence(@PathVariable Integer userId){
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
    public boolean checkPassword(@PathVariable Integer userId,
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
    public String updateUser(@RequestBody User updatedUser, @PathVariable Integer userId) throws UserNotFoundException, UserSettingsNotFoundException {

        User user = userService.getUserById(userId);

        userService.updateUser(updatedUser, user);

        return "user "+ userService.getUserById(userId).getUsername()+" was updated successfully";
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Integer userId) throws UserNotFoundException {

        User user = userService.getUserById(userId);
        String username = user.getUsername();
        userService.deleteUser(user);
        return "user "+username+" was successfully deleted from the database";
    }
}
