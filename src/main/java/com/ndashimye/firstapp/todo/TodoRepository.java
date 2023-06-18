package com.ndashimye.firstapp.todo;

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
public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("SELECT t from Todo t " +
            "INNER JOIN UserProject up ON t.project = up.project " +
            "LEFT JOIN BlacklistedUser bu ON t = bu.todo " +
            "WHERE up.user = :user " +
            "AND bu.blacklistedUserId IS NULL " +
            "ORDER BY t.position ASC")
    List<Todo> findAccessibleTodosOfUserAndOrderByPositionAsc(@Param("user") User user);

    @Query("SELECT t from Todo t " +
            "INNER JOIN UserProject up ON t.project = up.project " +
            "LEFT JOIN BlacklistedUser bu ON t = bu.todo " +
            "WHERE up.user = :user " +
            "AND bu.blacklistedUserId IS NULL " +
            "ORDER BY t.dueTime DESC")
    List<Todo> findAccessibleTodosOfUserAndOrderByDueTimeDesc(@Param("user") User user);

    @Query("SELECT t from Todo t " +
            "INNER JOIN UserProject up ON t.project = up.project " +
            "LEFT JOIN BlacklistedUser bu ON t = bu.todo " +
            "WHERE up.user = :user " +
            "AND bu.blacklistedUserId IS NULL " +
            "ORDER BY t.dueTime ASC")
    List<Todo> findAccessibleTodosOfUserAndOrderByDueTimeAsc(@Param("user") User user);

    @Query(value = "SELECT t.* from todo t " +
            "INNER JOIN user_project up ON t.project_id = up.project_id " +
            "LEFT JOIN blacklisted_user AS bu ON t.todo_id = bu.todo_id " +
            "WHERE up.user_id = :userId " +
            "AND t.due_time BETWEEN :startDate AND :endDate " +
            "AND bu.blacklisted_user_id IS NULL "+
            "ORDER BY t.due_time ASC",
            nativeQuery = true)
    List<Todo> findAccessibleTodosOfUserAndDueTimeBetween(Long userId, Timestamp startDate, Timestamp endDate);

    @Query("SELECT MAX(t.position) AS max_position " +
            "FROM Todo AS t " +
            "INNER JOIN UserProject AS up ON t.project = up.project " +
            "LEFT JOIN BlacklistedUser AS bu ON t = bu.todo " +
            "WHERE up.project = :project " +
            "AND bu.blacklistedUserId IS NULL")
    Integer getMaxPosition(@Param("project") Project project);

    @Query("SELECT t FROM Todo t " +
            "INNER JOIN UserProject up ON t.project = up.project " +
            "LEFT JOIN BlacklistedUser bu ON t = bu.todo " +
            "WHERE up.project = :project " +
            "AND t.position BETWEEN :start AND :end " +
            "AND bu.blacklistedUserId IS NULL " +
            "ORDER BY t.position ASC")
    List<Todo> findAccessibleTodosOfUserWithPositionsBetween
            (@Param("project") Project project, @Param("start") int start, @Param("end") int end);



    @Query("FROM Todo AS t " +
            "INNER JOIN UserProject up ON t.project = up.project " +
            "LEFT JOIN BlacklistedUser AS bu ON t = bu.todo " +
            "WHERE up.user = :user " +
            "AND t.project = :project " +
            "AND bu.blacklistedUserId IS NULL " +
            "ORDER BY t.position ASC")
    List<Todo> findByProjectAndUserAndOrderByPositionAsc
            (@Param("user") User user, @Param("project") Project project);


    @Query("FROM Todo AS t " +
            "INNER JOIN UserProject up ON t.project = up.project " +
            "LEFT JOIN BlacklistedUser AS bu ON t = bu.todo " +
            "WHERE up.user = :user " +
            "AND t.project = :project " +
            "AND bu.blacklistedUserId IS NULL " +
            "ORDER BY t.position DESC")
    List<Todo> findByProjectAndUserAndOrderByPositionDesc
            (@Param("user") User user, @Param("project") Project project);



    @Query("SELECT t from Todo t " +
            "INNER JOIN BlacklistedUser bu ON t = bu.todo " +
            "INNER JOIN UserProject up ON bu.userProject = up " +
            "WHERE bu.userProject = :userProject " +
            "ORDER BY t.position ASC")
    List<Todo> findBlacklistedTodosOfUserAndOrderByPositionAsc(@Param("userProject") UserProject userProject);

}
