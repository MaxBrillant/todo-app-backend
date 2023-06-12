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
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDTOMapper userDTOMapper;
    private final UserProfileService userProfileService;
    private final UserSettingsService userSettingsService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;



    //Service methods that handle all the operations related to users

    @Override
    public List<UserDTO> getAllUsers(){
        log.info("Fetching all users...");
        List<User> users = userRepository.findAll();
        log.info("All users were successfully fetched.");
        return users.stream()
                .map(userDTOMapper).collect(Collectors.toList());
    }

    @Override
    public User getUserById(Long userId) throws AppEntityNotFoundException {

        return userRepository.findById(userId)
                .orElseThrow(() -> new AppEntityNotFoundException(User.class));
    }

    @Override
    public UserDTO getUserDTOById(Long userId) throws AppEntityNotFoundException {

        log.info("Fetching user by ID: {}...", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppEntityNotFoundException(User.class));
        log.info("User of ID: {} and username: {} was successfully fetched."
                , user.getUserId(), user.getUsername());

        return userDTOMapper.apply(user);
    }

    @Override
    public UserDTO getUserByEmailAddress(String emailAddress)
            throws AppEntityNotFoundException {

        log.info("Fetching user by email address: {}...", emailAddress);
        User user = userRepository.findUserByEmailAddress(emailAddress)
                .orElseThrow(() -> new AppEntityNotFoundException(User.class));
        log.info("User of email address: {} was successfully fetched.", emailAddress);

        return userDTOMapper.apply(user);
    }

    @Override
    public UserDTO getUserByUsername(String username)
            throws AppEntityNotFoundException {

        log.info("Fetching user by username: {}...", username);
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new AppEntityNotFoundException(User.class));
        log.info("User of username: {} was successfully fetched.", username);

        return userDTOMapper.apply(user);
    }

    @Override
    public void addNewUser(UserRegistrationDTO userRegistrationDTO) {

        log.info("Adding a new user of username: {}...", userRegistrationDTO.username());

        User user = User.builder()
                .username(userRegistrationDTO.username())
                .emailAddress(userRegistrationDTO.email())
                .build();

        user.setPasswordSalt(BCrypt.gensalt());
        //TODO: This password has to be validated and hashed
        user.setPasswordHash(userRegistrationDTO.password());
//        user.setPasswordHash(passwordEncoder
//                .encode(user.getPasswordHash()+user.getPasswordSalt()));

        //Adding a profile and settings to the new user
        userProfileService.addNewUserProfile(user
                , UserProfile.builder()
                        .firstName(userRegistrationDTO.firstName())
                        .lastName(userRegistrationDTO.lastName())
                        .profileImageUrl(userRegistrationDTO.profileURL())
                        .build());

        userSettingsService.addNewUserSettings(user
                , UserSettings.builder()
                        .timeZone(userRegistrationDTO.timezone())
                        .theme(userRegistrationDTO.theme())
                        .language(userRegistrationDTO.language())
                        .build());

        userRepository.save(user);
        log.info("User of ID: {} and username: {} was successfully added."
                , user.getUserId(), user.getUsername());

    }

    @Override
    public void updateUser(UserRegistrationDTO updatedUser, Long userId)
            throws AppEntityNotFoundException {

        User user = getUserById(userId);
        log.info("Updating user of ID: {} and username: {}...", user.getUserId(), user.getUsername());

        user.setUsername(updatedUser.username());
        user.setEmailAddress(updatedUser.email());
        user.setPasswordSalt(BCrypt.gensalt());
        user.setPasswordHash(updatedUser.password());

        user.getProfile().setFirstName(updatedUser.firstName());
        user.getProfile().setLastName(updatedUser.lastName());
        user.getProfile().setProfileImageUrl(updatedUser.profileURL());

        user.getSettings().setTimeZone(updatedUser.timezone());
        user.getSettings().setLanguage(updatedUser.language());
        user.getSettings().setTheme(updatedUser.theme());
//            user.setPasswordHash(passwordEncoder
//                    .encode(updatedUser.getPasswordHash()+user.getPasswordSalt()));

        userRepository.save(user);
        log.info("User of ID: {} and username: {} was successfully updated."
                , user.getUserId(), updatedUser.username());
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
}
