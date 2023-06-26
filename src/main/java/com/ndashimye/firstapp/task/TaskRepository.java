package com.ndashimye.firstapp.task;

import com.ndashimye.firstapp.goal.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {


    @Query("SELECT t FROM Task t " +
            "WHERE t.goal = :goal " +
            "AND t.parentTask IS NULL " +
            "ORDER BY t.position ASC")
    List<Task> findByGoalAndOrderByPositionAsc(@Param("goal") Goal goal);

    @Query("SELECT t FROM Task t " +
            "WHERE t.goal = :goal " +
            "AND t.parentTask IS NULL " +
            "ORDER BY t.position DESC")
    List<Task> findByGoalAndOrderByPositionDesc(@Param("goal") Goal goal);

    @Query("SELECT t FROM Task t " +
            "WHERE t.goal = :goal " +
            "AND t.parentTask IS NULL " +
            "ORDER BY t.priorityLevel DESC")
    List<Task> findByGoalAndOrderByPriorityLevelDesc(@Param("goal") Goal goal);

    @Query("SELECT t FROM Task t " +
            "WHERE t.goal = :goal " +
            "AND t.parentTask IS NULL " +
            "AND t.CompletedByUser IS NOT NULL " +
            "ORDER BY t.position ASC")
    List<Task> findByCompletedTasks(@Param("goal") Goal goal);

    @Query("SELECT t FROM Task t " +
            "WHERE t.goal = :goal " +
            "AND t.parentTask IS NULL " +
            "AND t.CompletedByUser IS NULL " +
            "ORDER BY t.position ASC")
    List<Task> findByUncompletedTasks(@Param("goal") Goal goal);



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
            "AND t.CompletedByUser IS NOT NULL " +
            "ORDER BY t.position ASC")
    List<Task> findByCompletedChildTasks(@Param("task") Task Task);

    @Query("SELECT t FROM Task t " +
            "WHERE t.parentTask = :task " +
            "AND t.CompletedByUser IS NULL " +
            "ORDER BY t.position ASC")
    List<Task> findByUncompletedChildTasks(@Param("task") Task task);



    @Query("SELECT MAX(t.position) FROM Task t " +
            "WHERE t.goal = :goal " +
            "AND t.parentTask IS NULL")
    Integer getMaxPositionOfTasksWithNoParentTasks(@Param("goal") Goal goal);

    @Query("SELECT MAX(t.position) FROM Task t " +
            "WHERE t.goal = :goal " +
            "AND t.parentTask = :parentTask")
    Integer getMaxPositionOfTasksWithParentTasks(@Param("goal") Goal goal,
                                                 @Param("parentTask") Task parentTask);

    @Query("SELECT t FROM Task t " +
            "WHERE t.goal = :goal " +
            "AND t.parentTask IS NULL " +
            "AND t.position BETWEEN :start AND :end " +
            "ORDER BY t.position ASC")
    List<Task> findByGoalAndPositionWithNoParentTaskBetweenOrderByPositionAsc
            (@Param("goal") Goal goal, @Param("start") int firstPosition,
             @Param("end") int secondPosition);

    @Query("SELECT t FROM Task t " +
            "WHERE t.goal = :goal " +
            "AND t.parentTask = :parentTask " +
            "AND t.position BETWEEN :start AND :end " +
            "ORDER BY t.position ASC")
    List<Task> findByGoalAndPositionWithParentTaskBetweenOrderByPositionAsc
            (@Param("goal") Goal goal, @Param("parentTask") Task parentTask
                    , @Param("start") int firstPosition, @Param("end") int secondPosition);
}
