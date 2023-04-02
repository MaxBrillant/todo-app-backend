package com.ndashimye.firstapp.repository;

import com.ndashimye.firstapp.model.User;
import com.ndashimye.firstapp.model.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSettingsRepository extends JpaRepository<UserSettings, Integer> {
}
