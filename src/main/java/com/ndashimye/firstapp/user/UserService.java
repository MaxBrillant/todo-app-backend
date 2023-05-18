package com.ndashimye.firstapp.user;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.userprofile.UserProfile;
import com.ndashimye.firstapp.usersettings.UserSettings;

import java.util.List;

public interface UserService {

    //Service methods that handle all the operations related to users
    List<User> getAllUsers();

    User getUserById(Long userId) throws AppEntityNotFoundException;

    User getUserByEmailAddress(String emailAddress)
            throws AppEntityNotFoundException;

    User getUserByUsername(String username) throws AppEntityNotFoundException;

    void addNewUser(User user);

    void updateUser(User updatedUser, Long userId)
            throws AppEntityNotFoundException;

    void deleteUser(Long userId) throws AppEntityNotFoundException;


    /*
    Service methods that handle all the operations
    related to the relationship between users and their profiles/settings
    */
    void updateUserProfile(Long userId, UserProfile updatedUserProfile)
            throws AppEntityNotFoundException;

    void updateUserSettings(Long userId, UserSettings updatedUserSettings)
            throws AppEntityNotFoundException;
}
