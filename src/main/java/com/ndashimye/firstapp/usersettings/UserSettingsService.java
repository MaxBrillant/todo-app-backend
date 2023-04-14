package com.ndashimye.firstapp.usersettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserSettingsService {

    @Autowired
    UserSettingsRepository userSettingsRepository;
    public UserSettings getUserSettingsById(Integer userSettingsId) throws UserSettingsNotFoundException {

        Optional<UserSettings> userSettings = userSettingsRepository.findById(userSettingsId);

        if(!userSettings.isPresent()){
            throw new UserSettingsNotFoundException();
        }
        return userSettings.get();

    }

    public void addNewUserSettings(UserSettings userSettings) {
        userSettingsRepository.save(userSettings);
    }

    public void updateUserSettings(UserSettings updatedUserSettings, UserSettings userSettings) {

        if (Objects.nonNull(updatedUserSettings.getTimeZone()) && !updatedUserSettings.getTimeZone().equals("")) {
            userSettings.setTimeZone(updatedUserSettings.getTimeZone());
        }
        userSettingsRepository.save(userSettings);
    }

    public void deleteUserSettings(UserSettings userSettings) {
        userSettingsRepository.delete(userSettings);
    }
}
