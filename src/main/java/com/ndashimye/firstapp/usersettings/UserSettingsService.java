package com.ndashimye.firstapp.usersettings;

import com.ndashimye.firstapp.user.User;

public interface UserSettingsService {

    /*
    Service methods that handle all the operations
    related to the relationship between users and their settings
    */
    void addNewUserSettings(User user, UserSettings userSettings);

    void deleteUserSettings(User user);
}
