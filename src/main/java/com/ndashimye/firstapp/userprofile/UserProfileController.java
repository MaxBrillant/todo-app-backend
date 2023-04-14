package com.ndashimye.firstapp.userprofile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users/profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/{userProfileId}")
    public UserProfile getUserProfileById(@PathVariable Integer userProfileId)
            throws UserProfileNotFoundException {

        return userProfileService.getUserProfileById(userProfileId);
    }

    @PostMapping()
    public String addUserProfile(@RequestBody UserProfile userProfile) {

        userProfileService.addNewUserProfile(userProfile);

        return "profile of profile id "+userProfile.getUserProfileId()+" was added successfully";
    }

    @PutMapping("/{userProfileId}")
    public String updateUserProfile(@RequestBody UserProfile updatedUserProfile,
                                    @PathVariable Integer userProfileId) throws UserProfileNotFoundException {

        UserProfile userProfile = userProfileService.getUserProfileById(userProfileId);

        userProfileService.updateUserProfile(updatedUserProfile, userProfile);

        return "profile of profile id "+ userProfile.getUserProfileId()+" was updated successfully";
    }

    @DeleteMapping("/{userProfileId}")
    public String deleteUserProfile(@PathVariable Integer userProfileId) throws UserProfileNotFoundException {

        UserProfile userProfile = userProfileService.getUserProfileById(userProfileId);
        Integer id = userProfile.getUserProfileId();
        userProfileService.deleteUserProfile(userProfile);
        return "profile of profile id "+id+" was successfully deleted from the database";
    }
}
