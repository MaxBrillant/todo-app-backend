package com.ndashimye.firstapp.user;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.userprofile.UserProfile;
import com.ndashimye.firstapp.usersettings.UserSettings;

import java.util.List;

public interface UserService {

    //Service methods that handle all the operations related to users
    List<UserDTO> getAllUsers();

    User getUserById(Long userId) throws AppEntityNotFoundException;

    UserDTO getUserDTOById(Long userId) throws AppEntityNotFoundException;

    UserDTO getUserByEmailAddress(String emailAddress)
            throws AppEntityNotFoundException;

    UserDTO getUserByUsername(String username) throws AppEntityNotFoundException;

    void addNewUser(UserRegistrationDTO userRegistrationDTO);

    void updateUser(UserRegistrationDTO updatedUser, Long userId)
            throws AppEntityNotFoundException;

    void deleteUser(Long userId) throws AppEntityNotFoundException;
}
