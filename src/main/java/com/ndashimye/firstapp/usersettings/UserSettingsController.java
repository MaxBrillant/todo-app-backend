package com.ndashimye.firstapp.usersettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/settings")
public class UserSettingsController {
    @Autowired
    private UserSettingsService userSettingsService;

    @GetMapping("/{userSettingsId}")
    public UserSettings getUserSettingsById(@PathVariable Integer userSettingsId)
            throws UserSettingsNotFoundException {

        return userSettingsService.getUserSettingsById(userSettingsId);
    }
    @PostMapping()
    public String addUserSettings(@RequestBody UserSettings userSettings) {

        userSettingsService.addNewUserSettings(userSettings);

        return "settings of settings id "+userSettings.getUserSettingsId()+" were added successfully";
    }

    @PutMapping("/{userSettingsId}")
    public String updateUserSettings(@RequestBody UserSettings updatedUserSettings,
                                     @PathVariable Integer userSettingsId) throws UserSettingsNotFoundException {

        UserSettings userSettings = userSettingsService.getUserSettingsById(userSettingsId);

        userSettingsService.updateUserSettings(updatedUserSettings, userSettings);

        return "settings of settings id "+ userSettings.getUserSettingsId()+" were updated successfully";
    }

    @DeleteMapping("/{userSettingsId}")
    public String deleteUserSettings(@PathVariable Integer userSettingsId) throws UserSettingsNotFoundException {

        UserSettings userSettings = userSettingsService.getUserSettingsById(userSettingsId);
        Integer id = userSettings.getUserSettingsId();
        userSettingsService.deleteUserSettings(userSettings);
        return "settings of profile id "+id+" were successfully deleted from the database";
    }
}
