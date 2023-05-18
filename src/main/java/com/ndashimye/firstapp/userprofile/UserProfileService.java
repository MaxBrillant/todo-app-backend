package com.ndashimye.firstapp.userprofile;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.user.User;

public interface UserProfileService {

    /*
    Service methods that handle all the operations
    related to the relationship between users and their profile
    */
    void addNewUserProfile(User user, UserProfile userProfile);

    void deleteUserProfile(User user) throws AppEntityNotFoundException;
}
