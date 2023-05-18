package com.ndashimye.firstapp.user;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.userprofile.UserProfile;
import com.ndashimye.firstapp.userprofile.UserProfileService;
import com.ndashimye.firstapp.usersettings.Language;
import com.ndashimye.firstapp.usersettings.Theme;
import com.ndashimye.firstapp.usersettings.UserSettings;
import com.ndashimye.firstapp.usersettings.UserSettingsService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserProfileService userProfileService;
    private final UserSettingsService userSettingsService;
    private UserRepository userRepository;



    //Service methods that handle all the operations related to users

    @Override
    public List<User> getAllUsers(){
        log.info("Fetching all users...");
        List<User> users = userRepository.findAll();
        log.info("All users were successfully fetched.");
        return users;
    }

    @Override
    public User getUserById(Long userId) throws AppEntityNotFoundException {

        log.info("Fetching user by ID: {}...", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppEntityNotFoundException(User.class));
        log.info("User of ID: {} and username: {} was successfully fetched."
                , user.getUserId(), user.getUsername());

        return user;
    }

    @Override
    public User getUserByEmailAddress(String emailAddress)
            throws AppEntityNotFoundException {

        log.info("Fetching user by email address: {}...", emailAddress);
        User user = userRepository.findUserByEmailAddress(emailAddress)
                .orElseThrow(() -> new AppEntityNotFoundException(User.class));
        log.info("User of email address: {} was successfully fetched.", emailAddress);

        return user;
    }

    @Override
    public User getUserByUsername(String username)
            throws AppEntityNotFoundException {

        log.info("Fetching user by username: {}...", username);
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new AppEntityNotFoundException(User.class));
        log.info("User of username: {} was successfully fetched.", username);

        return user;
    }

    @Override
    public void addNewUser(User user) {

        log.info("Adding a new user of username: {}...", user.getUsername());
        user.setPassword(user.getPasswordHash());

        //Adding a profile and settings to the new user
        userProfileService.addNewUserProfile(user
                , UserProfile.builder()
                        .build());

        userSettingsService.addNewUserSettings(user
                , UserSettings.builder()
                        .timeZone("UTC")
                        .theme(Theme.LIGHT)
                        .language(Language.en_EN)
                        .build());

        userRepository.save(user);
        log.info("User of ID: {} and username: {} was successfully added."
                , user.getUserId(), user.getUsername());

    }

    @Override
    public void updateUser(User updatedUser, Long userId)
            throws AppEntityNotFoundException {

        User user = getUserById(userId);
        log.info("Updating user of ID: {} and username: {}...", user.getUserId(), user.getUsername());

        if (Objects.nonNull(updatedUser.getUsername()) && !updatedUser.getUsername().equals("")) {
            user.setUsername(updatedUser.getUsername());
        }
        if (Objects.nonNull(updatedUser.getEmailAddress()) && !updatedUser.getEmailAddress().equals("")) {
            user.setEmailAddress(updatedUser.getEmailAddress());
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

    @Override
    public void deleteUser(Long userId) throws AppEntityNotFoundException {

        User user = getUserById(userId);
        log.info("Deleting user of ID: {} and username: {}...", user.getUserId(), user.getUsername());

        //Delete the user profile and settings
        userProfileService.deleteUserProfile(user);
        userSettingsService.deleteUserSettings(user);

        User deletedUser = user;
        userRepository.delete(user);
        log.info("User of ID: {} and username: {} was successfully deleted."
                , deletedUser.getUserId(), deletedUser.getUsername());
    }



    /*

    Service methods that handle all the operations
    related to the relationship between users and their profiles/settings

    */

    @Override
    public void updateUserProfile(Long userId, UserProfile updatedUserProfile)
            throws AppEntityNotFoundException {

        User user = getUserById(userId);

        log.info("Updating the profile of user of ID: {} and username: {}..."
                , user.getUserId(), user.getUsername());

        if (Objects.nonNull(updatedUserProfile.getFirstName()) && !updatedUserProfile.getFirstName().equals("")) {
            user.getProfile().setFirstName(updatedUserProfile.getFirstName());
        }
        if (Objects.nonNull(updatedUserProfile.getLastName()) && !updatedUserProfile.getLastName().equals("")) {
            user.getProfile().setLastName(updatedUserProfile.getLastName());
        }
        if (Objects.nonNull(updatedUserProfile.getProfileImageUrl()) && !updatedUserProfile.getProfileImageUrl().equals("")) {
            user.getProfile().setProfileImageUrl(updatedUserProfile.getProfileImageUrl());
        }
        log.info("Profile of user of ID: {} and username: {} was successfully updated."
                , user.getUserId(), user.getUsername());
    }

    @Override
    public void updateUserSettings(Long userId, UserSettings updatedUserSettings)
            throws AppEntityNotFoundException {

        User user = getUserById(userId);

        log.info("Updating the settings of user of ID: {} and username: {}..."
                , user.getUserId(), user.getUsername());

        if (Objects.nonNull(updatedUserSettings.getTimeZone()) && !updatedUserSettings.getTimeZone().equals("")) {
            user.getSettings().setTimeZone(updatedUserSettings.getTimeZone());
        }
        log.info("Settings of user of ID: {} and username: {} were successfully updated."
                , user.getUserId(), user.getUsername());
    }
}
