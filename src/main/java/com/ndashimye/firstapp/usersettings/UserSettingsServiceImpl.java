package com.ndashimye.firstapp.usersettings;

import com.ndashimye.firstapp.user.User;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class UserSettingsServiceImpl implements UserSettingsService {

    private UserSettingsRepository userSettingsRepository;



    /*

    Service methods that handle all the operations
    related to the relationship between users and their settings

    */

    @Override
    public void addNewUserSettings(User user, UserSettings userSettings) {

        log.info("Adding settings to user of ID: {} and username: {}..."
                , user.getUserId(), user.getUsername());

        userSettingsRepository.save(userSettings);
        user.setSettings(userSettings);
        log.info("Settings of user of ID: {} and username: {} were successfully added."
                , user.getUserId(), user.getUsername());
    }

    @Override
    public void deleteUserSettings(User user) {

        log.info("Deleting settings of user of ID: {} and username: {}..."
                , user.getUserId(), user.getUsername());

        userSettingsRepository.delete(user.getSettings());
        log.info("Settings of user of ID: {} and username: {} was successfully deleted."
                , user.getUserId(), user.getUsername());
    }
}
