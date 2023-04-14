package com.ndashimye.firstapp.user;
import com.ndashimye.firstapp.ZonedDateTimeAttributeConverter;
import com.ndashimye.firstapp.userprofile.UserProfile;
import com.ndashimye.firstapp.userprofile.UserProfileNotFoundException;
import com.ndashimye.firstapp.usersettings.UserSettings;
import com.ndashimye.firstapp.usersettings.UserSettingsNotFoundException;
import com.ndashimye.firstapp.task.Task;
import com.ndashimye.firstapp.task.TaskRepository;
import com.ndashimye.firstapp.todo.Todo;
import com.ndashimye.firstapp.todo.TodoRepository;
import com.ndashimye.firstapp.userprofile.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TaskRepository taskRepository;


    @Autowired
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Integer userId) throws UserNotFoundException {

        Optional<User> user = userRepository.findById(userId);

        if(!user.isPresent()){
            throw new UserNotFoundException();
        }
        return user.get();
    }

    public UserProfile getUserProfileByUserId(Integer userId) throws UserProfileNotFoundException, UserNotFoundException {

        Optional<User> user = userRepository.findById(userId);

        if(!user.isPresent()){
            throw new UserNotFoundException();
        }
        if(!Objects.nonNull(user.get().getProfile())){
            throw new UserProfileNotFoundException();
        }
        Optional<UserProfile> userProfile = Optional.of(user.get().getProfile());

        if(!userProfile.isPresent() && !userProfile.isEmpty()){
            throw new UserProfileNotFoundException();
        }
        return userProfile.get();
    }


    public UserSettings getUserSettingsByUserId(Integer userId) throws UserNotFoundException, UserSettingsNotFoundException {
        Optional<User> user = userRepository.findById(userId);

        if(!user.isPresent()){
            throw new UserNotFoundException();
        }
        if(!Objects.nonNull(user.get().getSettings())){
            throw new UserSettingsNotFoundException();
        }
        Optional<UserSettings> userSettings = Optional.of(user.get().getSettings());

        if(!userSettings.isPresent() && !userSettings.isEmpty()){
            throw new UserSettingsNotFoundException();
        }
        return userSettings.get();
    }

    public List<Todo> getAllTodosByUserId(Integer userId) throws UserNotFoundException {

        Optional<User> user = userRepository.findById(userId);

        if(!user.isPresent()){
            throw new UserNotFoundException();
        }
        List<Todo> todos = todoRepository.findByUserTodo_UserOrderByUserTodo_OrderAsc(user.get());

        return todos;
    }


    public List<Todo> getAllTodosByUserIdOrderedByMostRecent(Integer userId) throws UserNotFoundException {

        Optional<User> user = userRepository.findById(userId);

        if(!user.isPresent()){
            throw new UserNotFoundException();
        }
        List<Todo> todos = todoRepository.findByUserOrderByDueTimeDesc(user.get());

        return todos;
    }

    public List<Todo> getAllTodosByUserIdOrderedByLeastRecent(Integer userId) throws UserNotFoundException {

        Optional<User> user = userRepository.findById(userId);

        if(!user.isPresent()){
            throw new UserNotFoundException();
        }
        List<Todo> todos = todoRepository.findByUserOrderByDueTimeAsc(user.get());

        return todos;
    }


    public List<Todo> getAllTodosByUserIdOrderedByPriority(Integer userId) throws UserNotFoundException {

        Optional<User> user = userRepository.findById(userId);

        if(!user.isPresent()){
            throw new UserNotFoundException();
        }
        List<Todo> todos = todoRepository.findByUserTodo_UserOrderByUserTodo_PriorityLevelDesc(user.get());

        return todos;
    }

    public List<Todo> getAllTodosBetweenDates
            (Integer userId, LocalDate startDate, LocalDate endDate) 
            throws UserNotFoundException, UserSettingsNotFoundException {

        Optional<User> user = userRepository.findById(userId);

        if(!user.isPresent()){
            throw new UserNotFoundException();
        }

        if(!Objects.nonNull(user.get().getSettings())){
            throw new UserSettingsNotFoundException();
        }
        ZoneId zoneId = ZoneId.of(user.get().getSettings().getTimeZone()); // or specify a specific timezone

        ZonedDateTime zonedStartDate = startDate.atStartOfDay(zoneId);
        ZonedDateTime zonedEndDate = endDate.plusDays(1).atStartOfDay(zoneId);

        List<Todo> todos = todoRepository.findByUserAndDueTimeBetween(user.get().getUserId(),
                Timestamp.valueOf(ZonedDateTimeAttributeConverter.toUtcZoneId(zonedStartDate).toLocalDateTime()),
                Timestamp.valueOf(ZonedDateTimeAttributeConverter.toUtcZoneId(zonedEndDate).toLocalDateTime()));

        return todos;
    }


    public List<Task> getAllTodayTasksOnDate
            (Integer userId, LocalDate date) throws UserNotFoundException, UserSettingsNotFoundException {

        Optional<User> user = userRepository.findById(userId);

        if(!user.isPresent()){
            throw new UserNotFoundException();
        }

        if(!Objects.nonNull(user.get().getSettings())){
            throw new UserSettingsNotFoundException();
        }

        ZoneId zoneId = ZoneId.of(user.get().getSettings().getTimeZone()); // or specify a specific timezone

        LocalDate nextDate = date.plusDays(1);

        ZonedDateTime taskDate = date.atStartOfDay(zoneId);
        ZonedDateTime nextZonedDate = nextDate.atStartOfDay(zoneId);

//        System.out.println("from "+ Timestamp.valueOf(ZonedDateTimeAttributeConverter.toUtcZoneId(taskDate).toLocalDateTime())+" to "+ nextZonedDate);

        List<Task> tasks = taskRepository.findAllByDateAndUser(user.get().getUserId(),
                Timestamp.valueOf(ZonedDateTimeAttributeConverter.toUtcZoneId(taskDate).toLocalDateTime()),
                Timestamp.valueOf(ZonedDateTimeAttributeConverter.toUtcZoneId(nextZonedDate).toLocalDateTime()));

        return tasks;
    }

    public User getUserByEmailAddress(String emailAddress) throws UserNotFoundException {

        Optional<User> user = userRepository.findUserByEmailAddress(emailAddress);

        if(!user.isPresent()){
            throw new UserNotFoundException();
        }
        return user.get();
    }

    public User getUserByUsername(String username) throws UserNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);

        if(!user.isPresent()){
            throw new UserNotFoundException();
        }
        return user.get();
    }
    public boolean userIdExists(Integer userId){
        return userRepository.existsByUserId(userId);
    }
    public boolean usernameExists(String username){
        return userRepository.existsByUsername(username);
    }

    public boolean emailAddressExists(String emailAddress){
        return userProfileRepository.existsByEmailAddress(emailAddress);
    }


    public boolean checkPassword(Integer userId, String password) throws UserNotFoundException {
        return getUserById(userId).checkPassword(password);
    }

    @Autowired
    public Integer getUsersCount(){
        return Math.toIntExact(userRepository.count());
    }

    public void addNewUser(User user){
        
        user.setPassword(user.getPasswordHash());
        userRepository.save(user);
    }

    public void updateUser(User updatedUser, User user) {

        if(Objects.nonNull(updatedUser.getProfile())){
            if(!updatedUser.getProfile().equals("")) {
                user.setProfile(updatedUser.getProfile());
            }
        }else{
            user.setProfile(updatedUser.getProfile());
        }


        if(Objects.nonNull(updatedUser.getSettings())){
            if(!updatedUser.getSettings().equals("")) {
                user.setSettings(updatedUser.getSettings());
            }
        }else{
            user.setSettings(updatedUser.getSettings());
        }


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
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

}
