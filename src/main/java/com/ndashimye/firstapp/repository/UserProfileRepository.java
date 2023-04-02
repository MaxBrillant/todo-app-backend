package com.ndashimye.firstapp.repository;

import com.ndashimye.firstapp.model.User;
import com.ndashimye.firstapp.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {

    boolean existsByEmailAddress(String emailAddress);
}
