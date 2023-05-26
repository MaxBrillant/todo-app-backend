package com.ndashimye.firstapp.userprofile;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.user.User;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private UserProfileRepository userProfileRepository;



    /*

    Service methods that handle all the operations
    related to the relationship between users and their profile

    */

    @Override
    public void addNewUserProfile(User user, UserProfile userProfile) {

        log.info("Adding a profile to user of ID: {} and username: {}..."
                , user.getUserId(), user.getUsername());

        userProfileRepository.save(userProfile);
        user.setProfile(userProfile);
        log.info("Profile of user of ID: {} and username: {} was successfully added."
                , user.getUserId(), user.getUsername());
    }


    @Override
    public void deleteUserProfile(User user) {

        log.info("Deleting profile of user of ID: {} and username: {}..."
                , user.getUserId(), user.getUsername());

        userProfileRepository.delete(user.getProfile());
        log.info("Profile of user of ID: {} and username: {} was successfully deleted."
                , user.getUserId(), user.getUsername());
    }
}
