package com.ndashimye.firstapp.goal;

import com.ndashimye.firstapp.project.Project;
import com.ndashimye.firstapp.user.User;
import com.ndashimye.firstapp.userproject.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

    @Query("SELECT g from Goal g " +
            "INNER JOIN UserProject up ON g.project = up.project " +
            "LEFT JOIN BlacklistedUser bu ON g = bu.goal " +
            "WHERE up.user = :user " +
            "AND bu.blacklistedUserId IS NULL " +
            "ORDER BY g.position ASC")
    List<Goal> findAccessibleGoalsOfUserAndOrderByPositionAsc(@Param("user") User user);

    @Query("SELECT g from Goal g " +
            "INNER JOIN UserProject up ON g.project = up.project " +
            "LEFT JOIN BlacklistedUser bu ON g = bu.goal " +
            "WHERE up.user = :user " +
            "AND bu.blacklistedUserId IS NULL " +
            "ORDER BY g.dueTime DESC")
    List<Goal> findAccessibleGoalsOfUserAndOrderByDueTimeDesc(@Param("user") User user);

    @Query("SELECT g from Goal g " +
            "INNER JOIN UserProject up ON g.project = up.project " +
            "LEFT JOIN BlacklistedUser bu ON g = bu.goal " +
            "WHERE up.user = :user " +
            "AND bu.blacklistedUserId IS NULL " +
            "ORDER BY g.dueTime ASC")
    List<Goal> findAccessibleGoalsOfUserAndOrderByDueTimeAsc(@Param("user") User user);

    @Query(value = "SELECT g.* from goal g " +
            "INNER JOIN user_project up ON g.project_id = up.project_id " +
            "LEFT JOIN blacklisted_user AS bu ON g.goal_id = bu.goal_id " +
            "WHERE up.user_id = :userId " +
            "AND g.due_time BETWEEN :startDate AND :endDate " +
            "AND bu.blacklisted_user_id IS NULL "+
            "ORDER BY g.due_time ASC",
            nativeQuery = true)
    List<Goal> findAccessibleGoalsOfUserAndDueTimeBetween(Long userId, Timestamp startDate, Timestamp endDate);

    @Query("SELECT MAX(g.position) AS max_position " +
            "FROM Goal AS g " +
            "INNER JOIN UserProject AS up ON g.project = up.project " +
            "LEFT JOIN BlacklistedUser AS bu ON g = bu.goal " +
            "WHERE up.project = :project " +
            "AND bu.blacklistedUserId IS NULL")
    Integer getMaxPosition(@Param("project") Project project);

    @Query("SELECT g FROM Goal g " +
            "INNER JOIN UserProject up ON g.project = up.project " +
            "LEFT JOIN BlacklistedUser bu ON g = bu.goal " +
            "WHERE up.project = :project " +
            "AND g.position BETWEEN :start AND :end " +
            "AND bu.blacklistedUserId IS NULL " +
            "ORDER BY g.position ASC")
    List<Goal> findAccessibleGoalsOfUserWithPositionsBetween
            (@Param("project") Project project, @Param("start") int start, @Param("end") int end);



    @Query("SELECT g FROM Goal AS g " +
            "INNER JOIN UserProject up ON g.project = up.project " +
            "LEFT JOIN BlacklistedUser AS bu ON g = bu.goal " +
            "WHERE up.user = :user " +
            "AND g.project = :project " +
            "AND bu.blacklistedUserId IS NULL " +
            "ORDER BY g.position ASC")
    List<Goal> findAccessibleGoalsByProjectAndUserAndOrderByPositionAsc
            (@Param("user") User user, @Param("project") Project project);


    @Query("FROM Goal AS g " +
            "INNER JOIN UserProject up ON g.project = up.project " +
            "LEFT JOIN BlacklistedUser AS bu ON g = bu.goal " +
            "WHERE up.user = :user " +
            "AND g.project = :project " +
            "AND bu.blacklistedUserId IS NULL " +
            "ORDER BY g.position DESC")
    List<Goal> findAccessibleGoalsByProjectAndUserAndOrderByPositionDesc
            (@Param("user") User user, @Param("project") Project project);



    @Query("SELECT g from Goal g " +
            "INNER JOIN BlacklistedUser bu ON g = bu.goal " +
            "INNER JOIN UserProject up ON bu.userProject = up " +
            "WHERE bu.userProject = :userProject " +
            "ORDER BY g.position ASC")
    List<Goal> findBlacklistedGoalsOfUserAndOrderByPositionAsc(@Param("userProject") UserProject userProject);

}
