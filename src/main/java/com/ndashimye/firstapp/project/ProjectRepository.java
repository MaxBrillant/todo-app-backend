package com.ndashimye.firstapp.project;

import com.ndashimye.firstapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT p from Project p " +
            "INNER JOIN UserProject up ON p = up.project " +
            "WHERE up.user = :user " +
            "ORDER BY up.position ASC")
    List<Project> findProjectsOfUserAndOrderByPositionAsc(@Param("user") User user);
}
