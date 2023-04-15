package com.ndashimye.firstapp.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    @Query("select u from User AS u where u.profile.emailAddress = ?1")
    Optional<User> findUserByEmailAddress(String emailAddress);
    boolean existsByUserId(Integer userId);
    boolean existsByUsername(String username);
}
