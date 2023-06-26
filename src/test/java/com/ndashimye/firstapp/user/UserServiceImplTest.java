package com.ndashimye.firstapp.user;

import com.ndashimye.firstapp.error.InvalidTimeFormatException;
import com.ndashimye.firstapp.goal.Goal;
import com.ndashimye.firstapp.goal.GoalRepository;
import com.ndashimye.firstapp.userprofile.UserProfile;
import com.ndashimye.firstapp.userprofile.UserProfileRepository;
import com.ndashimye.firstapp.usersettings.UserSettings;
import com.ndashimye.firstapp.usersettings.UserSettingsRepository;
import com.ndashimye.firstapp.blacklisteduser.BlacklistedUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private UserSettingsRepository userSettingsRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;


    @Test
    void getAllUsersTest() {
        // Define the behavior of the mock UserRepository
        User user = new User();
        user.setUsername("John Doe");
        List<User> expectedUsers = Collections.singletonList(user);
        when(userRepository.findAll()).thenReturn(expectedUsers);

        // Call the getAllUsers method in UserService
        List<User> actualUsers = userServiceImpl.getAllUsers();

        // Check if the returned list matches the expected list
        assertEquals(expectedUsers, actualUsers, "The returned list of users should match the expected list");

        // Verify the mock UserRepository's findAll method was called exactly once
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserByIdTest() throws UserNotFoundException {
        // Define the behavior of the mock UserRepository
        Long userId = 1L;
        User user = new User();
        user.setUserId(userId);
        user.setUsername("John Doe");
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Call the getUserById method in UserService
        User actualUser = userServiceImpl.getUserById(userId);

        // Check if the returned User matches the expected User
        assertEquals(user, actualUser, "The returned user should match the expected user");

        // Verify the mock UserRepository's findById method was called exactly once with the correct userId
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserByEmailTest() throws UserNotFoundException {
        // Define the behavior of the mock UserRepository
        Long userId = 1L;
        String email = "john@doe.com";
        User user = new User();
        user.setUserId(userId);
        user.setUsername("John Doe");
        user.setProfile(UserProfile.builder().emailAddress(email).build());

        when(userRepository.findUserByEmailAddress(email)).thenReturn(Optional.of(user));

        User actualUser = userServiceImpl.getUserByEmailAddress(email);

        // Check if the returned User matches the expected User
        assertEquals(user, actualUser, "The returned user should match the expected user");

        verify(userRepository, times(1)).findUserByEmailAddress(email);
    }


    @Test
    void getUserByUsernameTest() throws UserNotFoundException {
        // Define the behavior of the mock UserRepository
        Long userId = 1L;
        String username = "John Doe";
        User user = new User();
        user.setUserId(userId);
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User actualUser = userServiceImpl.getUserByUsername(username);

        // Check if the returned User matches the expected User
        assertEquals(user, actualUser, "The returned user should match the expected user");

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void getUserByIdThrowsUserNotFoundExceptionTest() {
        // Define the behavior of the mock UserRepository
        Long userId = Long.valueOf(1);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Call the getUserById method in UserService and assert that it throws UserNotFoundException
        assertThrows(UserNotFoundException.class, () -> userServiceImpl.getUserById(userId),
                "getUserById should throw UserNotFoundException when the user is not found");

        // Verify the mock UserRepository's findById method was called exactly once with the correct userId
        verify(userRepository, times(1)).findById(userId);
    }


    @Test
    public void getAllTodosByUserIdTest() throws UserNotFoundException {
        // Given
        Long userId = 1L;
        User user = User.builder().userId(userId).username("testUser").build();

        List<Goal> goals = new ArrayList<>();
        goals.add(Goal.builder().todoId(1L).name("Test Goal 1")
                .userTodo(new BlacklistedUser().builder().user(user).build()).build());
        goals.add(Goal.builder().todoId(2L).name("Test Goal 2")
                .userTodo(new BlacklistedUser().builder().user(user).build()).build());

        // When
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(goalRepository.findByUserOrderByUserTodoPositionAsc(user)).thenReturn(goals);

        // Then
        List<Goal> result = userServiceImpl.getAllTodosByUserId(userId);
        assertEquals(2, result.size());
        assertEquals(goals, result);
        assertEquals("Test Goal 1", result.get(0).getName());
        assertEquals("Test Goal 2", result.get(1).getName());
    }

    @Test
    void getAllTodosByUserIdOrderedByPriorityTest() throws UserNotFoundException {
        // Given
        Long userId = 1L;
        User user = User.builder().userId(userId).username("testUser").build();

        Goal goal1 = new Goal();
        goal1.setGoalId(1L);
        goal1.setName("Todo1");
        goal1.setUserTodo(BlacklistedUser.builder().priorityLevel(2).build());

        Goal goal2 = new Goal();
        goal2.setGoalId(2L);
        goal2.setName("Todo2");
        goal2.setUserTodo(BlacklistedUser.builder().priorityLevel(3).build());

        List<Goal> expectedGoals = new ArrayList<>();
        expectedGoals.add(goal1);
        expectedGoals.add(goal2);

        // When
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(goalRepository.findByUserOrderByTodoPriorityLevelDesc(user)).thenReturn(expectedGoals);

        List<Goal> goals = userServiceImpl.getAllTodosByUserIdOrderedByPriority(userId);

        // Then
        assertEquals(2, goals.size());
        assertEquals(expectedGoals, goals);
        assertEquals("Todo1", goals.get(0).getName());
        assertEquals("Todo2", goals.get(1).getName());
    }

    @Test
    void getAllTodosByUserIdOrderedByMostRecentTest() throws UserNotFoundException {
        // Given
        Long userId = 1L;
        User user = User.builder().userId(userId).username("testUser").build();

        Goal goal1 = new Goal();
        goal1.setGoalId(1L);
        goal1.setName("Todo1");
        goal1.setUserTodo(BlacklistedUser.builder().priorityLevel(2).build());
        goal1.setDueTime(ZonedDateTime.of(LocalDateTime.now().minusDays(3), ZoneId.of("UTC")));

        Goal goal2 = new Goal();
        goal2.setGoalId(2L);
        goal2.setName("Todo2");
        goal2.setUserTodo(BlacklistedUser.builder().priorityLevel(3).build());
        goal1.setDueTime(ZonedDateTime.of(LocalDateTime.now().minusDays(7), ZoneId.of("UTC")));

        List<Goal> expectedGoals = new ArrayList<>();
        expectedGoals.add(goal1);
        expectedGoals.add(goal2);

        // When
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(goalRepository.findByUserOrderByDueTimeDesc(user)).thenReturn(expectedGoals);

        List<Goal> goals = userServiceImpl.getAllTodosByUserIdOrderedByMostRecent(userId);

        // Then
        assertEquals(2, goals.size());
        assertEquals(expectedGoals, goals);
        assertEquals("Todo1", goals.get(0).getName());
        assertEquals("Todo2", goals.get(1).getName());
    }


    @Test
    void getAllTodosByUserIdOrderedByLeastRecentTest() throws UserNotFoundException {
        // Given
        Long userId = 1L;
        User user = User.builder().userId(userId).username("testUser").build();

        Goal goal1 = new Goal();
        goal1.setGoalId(1L);
        goal1.setName("Todo1");
        goal1.setUserTodo(BlacklistedUser.builder().priorityLevel(2).build());
        goal1.setDueTime(ZonedDateTime.of(LocalDateTime.now().minusDays(3), ZoneId.of("UTC")));

        Goal goal2 = new Goal();
        goal2.setGoalId(2L);
        goal2.setName("Todo2");
        goal2.setUserTodo(BlacklistedUser.builder().priorityLevel(3).build());
        goal1.setDueTime(ZonedDateTime.of(LocalDateTime.now().minusDays(7), ZoneId.of("UTC")));

        List<Goal> expectedGoals = new ArrayList<>();
        expectedGoals.add(goal1);
        expectedGoals.add(goal2);

        // When
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(goalRepository.findByUserOrderByDueTimeAsc(user)).thenReturn(expectedGoals);

        List<Goal> goals = userServiceImpl.getAllTodosByUserIdOrderedByLeastRecent(userId);

        // Then
        assertEquals(2, goals.size());
        assertEquals(expectedGoals, goals);
        assertEquals("Todo1", goals.get(0).getName());
        assertEquals("Todo2", goals.get(1).getName());
    }


    @Test
    void getAllTodosBetweenDatesTest() throws UserNotFoundException, InvalidTimeFormatException, UserSettingsNotFoundException {
        // Given
        Long userId = 1L;
        User user = User.builder().userId(userId).username("testUser")
                .settings(UserSettings.builder().timeZone("UTC").build()).build();

        Goal goal1 = new Goal();
        goal1.setGoalId(1L);
        goal1.setName("Todo1");
        goal1.setUserTodo(BlacklistedUser.builder().priorityLevel(2).build());
        goal1.setDueTime(ZonedDateTime.of(LocalDateTime.now().minusDays(3), ZoneId.of("UTC")));

        Goal goal2 = new Goal();
        goal2.setGoalId(2L);
        goal2.setName("Todo2");
        goal2.setUserTodo(BlacklistedUser.builder().priorityLevel(3).build());
        goal1.setDueTime(ZonedDateTime.of(LocalDateTime.now().minusDays(7), ZoneId.of("UTC")));

        List<Goal> expectedGoals = new ArrayList<>();
        expectedGoals.add(goal1);
        expectedGoals.add(goal2);

        // When
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(goalRepository.findByUserAndDueTimeBetween(eq(userId)
                , any(Timestamp.class)
                , any(Timestamp.class)))
                .thenReturn(expectedGoals);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Goal> goals = userServiceImpl.getAllTodosBetweenDates
                (userId, LocalDateTime.now().minusDays(3).format(formatter)
                        , LocalDateTime.now().minusDays(7).format(formatter));

        // Then
        assertEquals(2, goals.size());
        assertEquals(expectedGoals, goals);
        assertEquals("Todo1", goals.get(0).getName());
        assertEquals("Todo2", goals.get(1).getName());
    }


    @Test
    void addNewUserTest() {
        // Given
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");

        UserProfile userProfile = new UserProfile();
        UserSettings userSettings = new UserSettings();

        when(userRepository.save(user)).thenReturn(user);
        when(userProfileRepository.save(userProfile)).thenReturn(userProfile);
        when(userSettingsRepository.save(userSettings)).thenReturn(userSettings);

        // When
        userServiceImpl.addNewUser(user);
        // Then
        verify(userRepository, times(1)).save(user);
        verify(userProfileRepository, times(1)).save(userProfile);
        verify(userSettingsRepository, times(1)).save(userSettings);
    }


    @Test
    void updateUserTest() throws UserNotFoundException {
        // Given
        Long userId = 1L;
        User originalUser = new User();
        originalUser.setUserId(userId);
        originalUser.setUsername("originalUsername");
        originalUser.setPassword("originalPassword");

        User updatedUser = new User();
        updatedUser.setUsername("updatedUsername");
        updatedUser.setPassword("updatedPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.of(originalUser));

        // When
        userServiceImpl.updateUser(updatedUser, userId);

        // Then
        assertEquals(updatedUser.getUsername(), originalUser.getUsername());
        assertEquals(updatedUser.getLastLogin(), originalUser.getLastLogin());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(originalUser);
    }


    @Test
    void deleteUserTest() throws UserNotFoundException, UserSettingsNotFoundException, UserProfileNotFoundException {
        // Given
        Long userId = 1L;
        User user = new User();
        user.setUserId(userId);
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        user.setProfile(new UserProfile());
        user.setSettings(new UserSettings());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user)).thenReturn(Optional.empty());

        // When
        userServiceImpl.deleteUser(userId);

        // Then
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(user);
        verify(userProfileRepository, times(1)).delete(any(UserProfile.class));
        verify(userSettingsRepository, times(1)).delete(any(UserSettings.class));

        Optional<User> deletedUser = userRepository.findById(userId);
        assertFalse(deletedUser.isPresent());
    }


    @Test
    void addNewUserProfileTest() throws UserProfileNotFoundException {
        // Given
        User user = new User();
        user.setUserId(1L);
        user.setUsername("testUsername");
        user.setPassword("testPassword");

        UserProfile userProfile = new UserProfile();
        userProfile.setUserProfileId(1L);
        userProfile.setFirstName("John");
        userProfile.setLastName("Doe");

        // When
        userServiceImpl.addNewUserProfile(user, userProfile);

        // Then
        assertNotNull(user.getProfile());
        assertEquals(userProfile.getUserProfileId(), user.getProfile().getUserProfileId());
        assertEquals(userProfile.getFirstName(), user.getProfile().getFirstName());
        assertEquals(userProfile.getLastName(), user.getProfile().getLastName());
        verify(userProfileRepository, times(1)).save(userProfile);
    }

    @Test
    void updateUserProfileTest() throws UserNotFoundException, UserProfileNotFoundException {
        // Given
        Long userId = 1L;
        User user = new User();
        user.setUserId(userId);
        user.setUsername("testUsername");
        user.setPassword("testPassword");

        UserProfile userProfile = new UserProfile();
        userProfile.setFirstName("John");
        userProfile.setLastName("Doe");
        userProfile.setEmailAddress("john.doe@example.com");
        userProfile.setProfileImageUrl("https://example.com/profile.jpg");
        user.setProfile(userProfile);

        UserProfile updatedUserProfile = new UserProfile();
        updatedUserProfile.setFirstName("Jane");
        updatedUserProfile.setLastName("Doe");
        updatedUserProfile.setEmailAddress("jane.doe@example.com");
        updatedUserProfile.setProfileImageUrl("https://example.com/profile_new.jpg");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        userServiceImpl.updateUserProfile(userId, updatedUserProfile);

        // Then
        assertEquals(updatedUserProfile.getFirstName(), user.getProfile().getFirstName());
        assertEquals(updatedUserProfile.getLastName(), user.getProfile().getLastName());
        assertEquals(updatedUserProfile.getEmailAddress(), user.getProfile().getEmailAddress());
        assertEquals(updatedUserProfile.getProfileImageUrl(), user.getProfile().getProfileImageUrl());
    }


    @Test
    void deleteUserProfileTest() throws UserProfileNotFoundException {
        // Given
        User user = new User();
        user.setUserId(1L);
        user.setUsername("testUsername");
        user.setPassword("testPassword");

        UserProfile userProfile = new UserProfile();
        userProfile.setUserProfileId(1L);
        userProfile.setFirstName("John");
        userProfile.setLastName("Doe");
        user.setProfile(userProfile);

        // When
        userServiceImpl.deleteUserProfile(user);

        // Then
        verify(userProfileRepository, times(1)).delete(userProfile);
        Optional<UserProfile> deletedProfile = userProfileRepository.findById(1L);
        assertFalse(deletedProfile.isPresent());
    }


    @Test
    void addNewUserSettingsTest() throws UserSettingsNotFoundException {
        // Given
        User user = new User();
        user.setUserId(1L);
        user.setUsername("testUsername");
        user.setPassword("testPassword");

        UserSettings userSettings = new UserSettings();
        userSettings.setUserSettingsId(1L);
        userSettings.setTimeZone("UTC");

        // When
        userServiceImpl.addNewUserSettings(user, userSettings);

        // Then
        assertNotNull(user.getSettings());
        assertEquals(userSettings.getUserSettingsId(), user.getSettings().getUserSettingsId());
        assertEquals(userSettings.getTimeZone(), user.getSettings().getTimeZone());
        verify(userSettingsRepository, times(1)).save(userSettings);
    }

    @Test
    void updateUserSettingsTest() throws UserSettingsNotFoundException, UserNotFoundException {
        // Given
        Long userId = 1L;
        User user = new User();
        user.setUserId(userId);
        user.setUsername("testUsername");
        user.setPassword("testPassword");

        UserSettings userSettings = new UserSettings();
        userSettings.setUserSettingsId(1L);
        userSettings.setTimeZone("UTC");
        user.setSettings(userSettings);

        UserSettings updatedUserSettings = new UserSettings();
        updatedUserSettings.setTimeZone("UTC+3");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        userServiceImpl.updateUserSettings(userId, updatedUserSettings);

        // Then
        assertEquals(updatedUserSettings.getTimeZone(), user.getSettings().getTimeZone());
    }


    @Test
    void deleteUserSettingsTest() throws UserSettingsNotFoundException {
        // Given
        User user = new User();
        user.setUserId(1L);
        user.setUsername("testUsername");
        user.setPassword("testPassword");

        UserSettings userSettings = new UserSettings();
        userSettings.setUserSettingsId(1L);
        userSettings.setTimeZone("UTC");
        user.setSettings(userSettings);

        // When
        userServiceImpl.deleteUserSettings(user);

        // Then
        verify(userSettingsRepository, times(1)).delete(userSettings);
        Optional<UserSettings> deletedSettings = userSettingsRepository.findById(1L);
        assertFalse(deletedSettings.isPresent());
    }

}
