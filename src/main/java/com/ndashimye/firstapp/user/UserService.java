package com.ndashimye.firstapp.user;

import com.ndashimye.firstapp.ZonedDateTimeAttributeConverter;
import com.ndashimye.firstapp.error.InvalidTimeFormatException;
import com.ndashimye.firstapp.userprofile.UserProfile;
import com.ndashimye.firstapp.usersettings.UserSettings;
import com.ndashimye.firstapp.usersettings.UserSettingsNotFoundException;
import com.ndashimye.firstapp.todo.Todo;
import com.ndashimye.firstapp.todo.TodoRepository;
import com.ndashimye.firstapp.userprofile.UserProfileRepository;
import com.ndashimye.firstapp.usersettings.UserSettingsRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserSettingsRepository userSettingsRepository;

    @Autowired
    private TodoRepository todoRepository;


    @Autowired
    public List<User> getAllUsers(){
        log.info("Fetching all users...");
        List<User> users = userRepository.findAll();
        log.info("All users were successfully fetched.");
        return users;
    }

    public User getUserById(Long userId) throws UserNotFoundException {

        log.info("Fetching user by ID: {}...", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
        log.info("User of ID: {} and username: {} was successfully fetched."
                , user.getUserId(), user.getUsername());

        return user;
    }

    public List<Todo> getAllTodosByUserId(Long userId) throws UserNotFoundException {

        User user = getUserById(userId);
        log.info("Fetching all todos of user of ID: {} and username: {}..."
                , user.getUserId(), user.getUsername());

        List<Todo> todos = todoRepository.findByUserTodo_UserOrderByUserTodo_PositionAsc(user);
        log.info("All todos of user of ID: {} and username: {} were " +
                "successfully fetched.", user.getUserId(), user.getUsername());

        return todos;
    }


    public List<Todo> getAllTodosByUserIdOrderedByPriority(Long userId)
            throws UserNotFoundException {

        User user = getUserById(userId);
        log.info("Fetching all todos of user of ID: {} and username: {} ordered by priority..."
                , user.getUserId(), user.getUsername());

        List<Todo> todos = todoRepository.findByUserTodo_UserOrderByUserTodo_PriorityLevelDesc(user);
        log.info("All todos of user of ID: {} and username: {} ordered by priority " +
                "were successfully fetched.", user.getUserId(), user.getUsername());

        return todos;
    }


    public List<Todo> getAllTodosByUserIdOrderedByMostRecent(Long userId)
            throws UserNotFoundException {

        User user = getUserById(userId);
        log.info("Fetching all todos of user of ID: {} and username: {} ordered from most to least recent..."
                , user.getUserId(), user.getUsername());

        List<Todo> todos = todoRepository.findByUserOrderByDueTimeDesc(user);
        log.info("All todos of user of ID: {} and username: {} ordered from most to " +
                "least recent were successfully fetched.", user.getUserId(), user.getUsername());

        return todos;
    }

    public List<Todo> getAllTodosByUserIdOrderedByLeastRecent(Long userId)
            throws UserNotFoundException {

        User user = getUserById(userId);
        log.info("Fetching all todos of user of ID: {} and username: {} ordered from least to most recent..."
                , user.getUserId(), user.getUsername());

        List<Todo> todos = todoRepository.findByUserOrderByDueTimeAsc(user);
        log.info("All todos of user of ID: {} and username: {} ordered from least to " +
                "most recent were successfully fetched.", user.getUserId(), user.getUsername());

        return todos;
    }


    public List<Todo> getAllTodosBetweenDates
            (Long userId, String start, String end)
            throws UserNotFoundException, UserSettingsNotFoundException, InvalidTimeFormatException {


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

        User user = getUserById(userId);

        log.info("Getting the timezone information from the user settings...");

        UserSettings userSettings = Optional.ofNullable(user.getSettings())
                .orElseThrow(() -> new UserSettingsNotFoundException());

        ZoneId zoneId = ZoneId.of(userSettings.getTimeZone()); // or specify a specific timezone


        log.info("Successfully accessed the user's timezone information.");

        ZonedDateTime zonedStartDate = startDate.atStartOfDay(zoneId);
        ZonedDateTime zonedEndDate = endDate.plusDays(1).atStartOfDay(zoneId);


        log.info("Fetching all todos of user of ID: {} and username: {} between {} and {}..."
                , user.getUserId(), user.getUsername(), start, end);

        List<Todo> todos = todoRepository.findByUserAndDueTimeBetween(user.getUserId(),
                Timestamp.valueOf(ZonedDateTimeAttributeConverter.toUtcZoneId(zonedStartDate).toLocalDateTime()),
                Timestamp.valueOf(ZonedDateTimeAttributeConverter.toUtcZoneId(zonedEndDate).toLocalDateTime()));

        log.info("All todos of user of ID: {} and username: {} between {} and {} " +
                "were successfully fetched.", user.getUserId(), user.getUsername(), start, end);

        return todos;
    }

    public User getUserByEmailAddress(String emailAddress) throws UserNotFoundException {

        log.info("Fetching user by email address: {}...", emailAddress);
        User user = userRepository.findUserByEmailAddress(emailAddress)
                .orElseThrow(() -> new UserNotFoundException());
        log.info("User of email address: {} was successfully fetched.", emailAddress);

        return user;
    }

    public User getUserByUsername(String username) throws UserNotFoundException {

        log.info("Fetching user by username: {}...", username);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException());
        log.info("User of username: {} was successfully fetched.", username);

        return user;
    }
    public boolean userIdExists(Long userId){
        return userRepository.existsByUserId(userId);
    }
    public boolean usernameExists(String username){
        return userRepository.existsByUsername(username);
    }

    public boolean emailAddressExists(String emailAddress){
        return userProfileRepository.existsByEmailAddress(emailAddress);
    }


    public boolean checkPassword(Long userId, String password) throws UserNotFoundException {
        return getUserById(userId).checkPassword(password);
    }

    @Autowired
    public Integer getUsersCount(){
        return Math.toIntExact(userRepository.count());
    }

    public void addNewUser(User user){

        log.info("Adding a new user of username: {}...", user.getUsername());
        user.setPassword(user.getPasswordHash());
        userRepository.save(user);
        log.info("User of ID: {} and username: {} was successfully added."
                , user.getUserId(), user.getUsername());
    }

    public void updateUser(User updatedUser, Long userId) throws UserNotFoundException {

        User user = getUserById(userId);
        log.info("Updating user of ID: {} and username: {}...", user.getUserId(), user.getUsername());

        if (Objects.nonNull(updatedUser.getUsername()) && !updatedUser.getUsername().equals("")) {
            user.setUsername(updatedUser.getUsername());
        }
        if (Objects.nonNull(updatedUser.getPasswordHash()) && !updatedUser.getPasswordHash().equals("")) {
            user.setPassword(updatedUser.getPasswordHash());
        }
        if (Objects.nonNull(updatedUser.getLastLogin()) && !updatedUser.getLastLogin().equals("")) {
            user.setLastLogin(updatedUser.getLastLogin());
        }

        userRepository.save(user);
        log.info("User of ID: {} and username: {} was successfully updated."
                , user.getUserId(), updatedUser.getUsername());
    }

    public void deleteUser(Long userId) throws UserNotFoundException {

        User user = getUserById(userId);
        log.info("Deleting user of ID: {} and username: {}...", user.getUserId(), user.getUsername());
        User deletedUser = user;
        userRepository.delete(user);
        log.info("User of ID: {} and username: {} was successfully deleted."
                , deletedUser.getUserId(), deletedUser.getUsername());
    }




    public void addNewUserProfile(Long userId, UserProfile userProfile)
            throws UserNotFoundException {

        User user = getUserById(userId);
        log.info("Adding a profile to user of ID: {} and username: {}..."
                , user.getUserId(), user.getUsername());

        if(Objects.isNull(user.getProfile())) {
            userProfileRepository.save(userProfile);
            user.setProfile(userProfile);
            log.info("Profile of user of ID: {} and username: {} was successfully added."
                    , user.getUserId(), user.getUsername());
        }else {
            log.error("ERROR: User of ID: {} and username: {} already has an existing profile, try to update it instead."
                    , user.getUserId(), user.getUsername());
        }
    }

    public void updateUserProfile(Long userId, UserProfile updatedUserProfile)
            throws UserNotFoundException {

        User user = getUserById(userId);

        log.info("Updating the profile of user of ID: {} and username: {}..."
                , user.getUserId(), user.getUsername());

        if (Objects.nonNull(user.getProfile())) {
            if (Objects.nonNull(updatedUserProfile.getFirstName()) && !updatedUserProfile.getFirstName().equals("")) {
                user.getProfile().setFirstName(updatedUserProfile.getFirstName());
            }
            if (Objects.nonNull(updatedUserProfile.getLastName()) && !updatedUserProfile.getLastName().equals("")) {
                user.getProfile().setLastName(updatedUserProfile.getLastName());
            }
            if (Objects.nonNull(updatedUserProfile.getEmailAddress()) && !updatedUserProfile.getEmailAddress().equals("")) {
                user.getProfile().setEmailAddress(updatedUserProfile.getEmailAddress());
            }
            if (Objects.nonNull(updatedUserProfile.getProfileImageUrl()) && !updatedUserProfile.getProfileImageUrl().equals("")) {
                user.getProfile().setProfileImageUrl(updatedUserProfile.getProfileImageUrl());
            }
            log.info("Profile of user of ID: {} and username: {} was successfully updated."
                    , user.getUserId(), user.getUsername());
        }else {
            log.error("ERROR: User of ID: {} and username: {} doesn't have a profile yet, " +
                    "try to add a profile to the user instead.", user.getUserId(), user.getUsername());
        }
    }
    public void deleteUserProfile(Long userId) throws UserNotFoundException {

        User user = getUserById(userId);

        log.info("Deleting the profile of user of ID: {} and username: {}..."
                , user.getUserId(), user.getUsername());

        if (Objects.nonNull(user.getProfile())) {
            userProfileRepository.delete(user.getProfile());
            log.info("Profile of user of ID: {} and username: {} was successfully deleted."
                    , user.getUserId(), user.getUsername());
        }else {
            log.error("ERROR: User of ID: {} and username: {} doesn't have a profile yet, " +
                    "try to add a profile to the user instead.", user.getUserId(), user.getUsername());
        }
    }



    public void addNewUserSettings(Long userId, UserSettings userSettings)
            throws UserNotFoundException {

        User user = getUserById(userId);

        log.info("Adding settings to user of ID: {} and username: {}..."
                , user.getUserId(), user.getUsername());

        if(Objects.isNull(user.getSettings())) {
            userSettingsRepository.save(userSettings);
            user.setSettings(userSettings);
            log.info("Settings of user of ID: {} and username: {} were successfully added."
                    , user.getUserId(), user.getUsername());
        }else {
            log.error("ERROR: User of ID: {} and username: {} already has existing settings, try to update them instead."
                    , user.getUserId(), user.getUsername());
        }
    }

    public void updateUserSettings(Long userId, UserSettings updatedUserSettings)
            throws UserNotFoundException {

        User user = getUserById(userId);

        log.info("Updating the settings of user of ID: {} and username: {}..."
                , user.getUserId(), user.getUsername());

        if (Objects.nonNull(user.getSettings())) {
            if (Objects.nonNull(updatedUserSettings.getTimeZone()) && !updatedUserSettings.getTimeZone().equals("")) {
                user.getSettings().setTimeZone(updatedUserSettings.getTimeZone());
            }
            log.info("Settings of user of ID: {} and username: {} were successfully updated."
                    , user.getUserId(), user.getUsername());
        }else {
            log.error("ERROR: User of ID: {} and username: {} doesn't have settings yet, " +
                    "try to add settings to the user instead.", user.getUserId(), user.getUsername());
        }
    }

    public void deleteUserSettings(Long userId) throws UserNotFoundException {

        User user = getUserById(userId);

        log.info("Deleting the settings of user of ID: {} and username: {}..."
                , user.getUserId(), user.getUsername());

        if(Objects.nonNull(user.getSettings())) {
            userSettingsRepository.delete(user.getSettings());
            log.info("Settings of user of ID: {} and username: {} were successfully deleted."
                    , user.getUserId(), user.getUsername());
        }else {
            log.error("ERROR: User of ID: {} and username: {} doesn't have settings yet, " +
                    "try to add settings to the user instead.", user.getUserId(), user.getUsername());
        }
    }

}
