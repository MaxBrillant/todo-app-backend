package com.ndashimye.firstapp.task;

import com.ndashimye.firstapp.project.Project;
import com.ndashimye.firstapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {


    @Query("SELECT t FROM Task t " +
            "WHERE t.project = :project " +
            "AND t.parentTask IS NULL " +
            "ORDER BY t.position ASC")
    List<Task> findByProjectAndOrderByPositionAsc(@Param("project") Project project);

    @Query("SELECT t FROM Task t " +
            "WHERE t.project = :project " +
            "AND t.parentTask IS NULL " +
            "ORDER BY t.position DESC")
    List<Task> findByProjectAndOrderByPositionDesc(@Param("project") Project project);

    @Query("SELECT t FROM Task t " +
            "WHERE t.project = :project " +
            "AND t.parentTask IS NULL " +
            "ORDER BY t.priorityLevel DESC")
    List<Task> findByProjectAndOrderByPriorityLevelDesc(@Param("project") Project project);

    @Query("SELECT t FROM Task t " +
            "WHERE t.project = :project " +
            "AND t.parentTask IS NULL " +
            "AND t.completedByUser IS NOT NULL " +
            "ORDER BY t.position ASC")
    List<Task> findByCompletedTasks(@Param("project") Project project);

    @Query("SELECT t FROM Task t " +
            "WHERE t.project = :project " +
            "AND t.parentTask IS NULL " +
            "AND t.completedByUser IS NULL " +
            "ORDER BY t.position ASC")
    List<Task> findByUncompletedTasks(@Param("project") Project project);



    @Query("SELECT t FROM Task t " +
            "WHERE t.parentTask = :task " +
            "ORDER BY t.position ASC")
    List<Task> findByParentTaskAndOrderByPositionAsc(@Param("task") Task task);

    @Query("SELECT t FROM Task t " +
            "WHERE t.parentTask = :task " +
            "ORDER BY t.position DESC")
    List<Task> findByParentTaskAndOrderByPositionDesc(@Param("task") Task task);

    @Query("SELECT t FROM Task t " +
            "WHERE t.parentTask = :task " +
            "ORDER BY t.priorityLevel DESC")
    List<Task> findByParentTaskAndOrderByPriorityLevelDesc(@Param("task") Task task);

    @Query("SELECT t FROM Task t " +
            "WHERE t.parentTask = :task " +
            "AND t.completedByUser IS NOT NULL " +
            "ORDER BY t.position ASC")
    List<Task> findByCompletedChildTasks(@Param("task") Task Task);

    @Query("SELECT t FROM Task t " +
            "WHERE t.parentTask = :task " +
            "AND t.completedByUser IS NULL " +
            "ORDER BY t.position ASC")
    List<Task> findByUncompletedChildTasks(@Param("task") Task task);



    @Query("SELECT MAX(t.position) FROM Task t " +
            "WHERE t.project = :project " +
            "AND t.parentTask IS NULL")
    Integer getMaxPositionOfTasksWithNoParentTasks(@Param("project") Project project);

    @Query("SELECT MAX(t.position) FROM Task t " +
            "WHERE t.project = :project " +
            "AND t.parentTask = :parentTask")
    Integer getMaxPositionOfTasksWithParentTasks(@Param("project") Project project,
                                                 @Param("parentTask") Task parentTask);

    @Query("SELECT t FROM Task t " +
            "WHERE t.project = :project " +
            "AND t.parentTask IS NULL " +
            "AND t.position BETWEEN :start AND :end " +
            "ORDER BY t.position ASC")
    List<Task> findByProjectAndPositionWithNoParentTaskBetweenOrderByPositionAsc
            (@Param("project") Project project, @Param("start") int firstPosition,
             @Param("end") int secondPosition);

    @Query("SELECT t FROM Task t " +
            "WHERE t.project = :project " +
            "AND t.parentTask = :parentTask " +
            "AND t.position BETWEEN :start AND :end " +
            "ORDER BY t.position ASC")
    List<Task> findByProjectAndPositionWithParentTaskBetweenOrderByPositionAsc
            (@Param("project") Project project, @Param("parentTask") Task parentTask
                    , @Param("start") int firstPosition, @Param("end") int secondPosition);





    @Query("SELECT t from Task t " +
            "INNER JOIN UserProject up ON t.project = up.project " +
            "WHERE up.user = :user " +
            "ORDER BY t.position ASC")
    List<Task> findTasksOfUserAndOrderByPositionAsc(@Param("user") User user);

    @Query("SELECT t from Task t " +
            "INNER JOIN UserProject up ON t.project = up.project " +
            "WHERE up.user = :user " +
            "ORDER BY t.dueTime DESC")
    List<Task> findTasksOfUserAndOrderByDueTimeDesc(@Param("user") User user);

    @Query("SELECT t from Task t " +
            "INNER JOIN UserProject up ON t.project = up.project " +
            "WHERE up.user = :user " +
            "ORDER BY t.dueTime ASC")
    List<Task> findTasksOfUserAndOrderByDueTimeAsc(@Param("user") User user);

    @Query(value = "SELECT t.* from task t " +
            "INNER JOIN user_project up ON t.project_id = up.project_id " +
            "WHERE up.user_id = :userId " +
            "AND t.due_time BETWEEN :startDate AND :endDate " +
            "ORDER BY t.due_time ASC",
            nativeQuery = true)
    List<Task> findTasksOfUserAndDueTimeBetween(Long userId, Timestamp startDate, Timestamp endDate);
}
