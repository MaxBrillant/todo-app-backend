package com.ndashimye.firstapp.userprofile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    boolean existsByEmailAddress(String emailAddress);
}
