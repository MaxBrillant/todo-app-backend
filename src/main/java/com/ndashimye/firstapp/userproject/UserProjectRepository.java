package com.ndashimye.firstapp.userproject;

import com.ndashimye.firstapp.project.Project;
import com.ndashimye.firstapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface UserProjectRepository extends JpaRepository<UserProject, Long> {
    Optional<UserProject> findByUserAndProject(User user, Project project);

    @Query("SELECT MAX(up.position) AS max_position " +
            "FROM UserProject AS up " +
            "WHERE up.user = :user")
    Integer getMaxPosition(@Param("user") User user);

    @Query("SELECT up FROM UserProject up " +
            "INNER JOIN Project p ON p = up.project " +
            "WHERE up.user = :user " +
            "AND up.position BETWEEN :start AND :end " +
            "ORDER BY up.position ASC")
    List<UserProject> findProjectsWithPositionsBetween
            (@Param("user") User user, @Param("start") int start, @Param("end") int end);
}
