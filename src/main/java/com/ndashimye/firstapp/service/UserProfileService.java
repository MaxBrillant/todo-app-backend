package com.ndashimye.firstapp.service;

import com.ndashimye.firstapp.error.UserProfileNotFoundException;
import com.ndashimye.firstapp.model.User;
import com.ndashimye.firstapp.model.UserProfile;
import com.ndashimye.firstapp.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserProfileService {

    @Autowired
    public UserProfileRepository userProfileRepository;

    public UserProfile getUserProfileById(Integer userProfileId) throws UserProfileNotFoundException {

        Optional<UserProfile> userProfile = userProfileRepository.findById(userProfileId);

        if(!userProfile.isPresent()){
            throw new UserProfileNotFoundException();
        }
        return userProfile.get();
    }

    public void addNewUserProfile(UserProfile userProfile) {
        userProfileRepository.save(userProfile);
    }

    public void updateUserProfile(UserProfile updatedUserProfile, UserProfile userProfile) {

        if (Objects.nonNull(updatedUserProfile.getFirstName()) && !updatedUserProfile.getFirstName().equals("")) {
            userProfile.setFirstName(updatedUserProfile.getFirstName());
        }
        if (Objects.nonNull(updatedUserProfile.getLastName()) && !updatedUserProfile.getLastName().equals("")) {
            userProfile.setLastName(updatedUserProfile.getLastName());
        }
        if (Objects.nonNull(updatedUserProfile.getEmailAddress()) && !updatedUserProfile.getEmailAddress().equals("")) {
            userProfile.setEmailAddress(updatedUserProfile.getEmailAddress());
        }
        if (Objects.nonNull(updatedUserProfile.getProfileImageUrl()) && !updatedUserProfile.getProfileImageUrl().equals("")) {
            userProfile.setProfileImageUrl(updatedUserProfile.getProfileImageUrl());
        }

        userProfileRepository.save(userProfile);
    }
    public void deleteUserProfile(UserProfile userProfile) {
        userProfileRepository.delete(userProfile);
    }

}
