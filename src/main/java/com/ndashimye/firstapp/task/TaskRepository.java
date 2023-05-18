package com.ndashimye.firstapp.task;

import com.ndashimye.firstapp.todo.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {


    @Query("SELECT t FROM Task t " +
            "WHERE t.todo = :todo " +
            "AND t.parentTask IS NULL " +
            "ORDER BY t.position ASC")
    List<Task> findByTodoAndOrderByPositionAsc(@Param("todo") Todo todo);

    @Query("SELECT t FROM Task t " +
            "WHERE t.todo = :todo " +
            "AND t.parentTask IS NULL " +
            "ORDER BY t.priorityLevel DESC")
    List<Task> findByTodoAndOrderByPriorityLevelDesc(@Param("todo") Todo todo);

    @Query("SELECT t FROM Task t " +
            "WHERE t.todo = :todo " +
            "AND t.parentTask IS NULL " +
            "AND t.CompletedByUser IS NOT NULL " +
            "ORDER BY t.position ASC")
    List<Task> findByCompletedTasks(@Param("todo") Todo todo);

    @Query("SELECT t FROM Task t " +
            "WHERE t.todo = :todo " +
            "AND t.parentTask IS NULL " +
            "AND t.CompletedByUser IS NULL " +
            "ORDER BY t.position ASC")
    List<Task> findByUncompletedTasks(@Param("todo") Todo todo);



    @Query("SELECT t FROM Task t " +
            "WHERE t.parentTask = :task " +
            "ORDER BY t.position ASC")
    List<Task> findByParentTaskAndOrderByPositionAsc(@Param("task") Task task);

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



    @Query("SELECT t FROM Task t " +
            "WHERE t.todo = :todo " +
            "AND t.parentTask IS NULL " +
            "ORDER BY t.position DESC")
    Optional<Task> findLastTaskByTodoAndNoParentTaskAndOrderByPosition(@Param("todo") Todo todo);

    @Query("SELECT t FROM Task t " +
            "WHERE t.todo = :todo " +
            "AND t.parentTask = :parentTask " +
            "AND t.position BETWEEN :start AND :end " +
            "ORDER BY t.position ")
    List<Task> findByTodoAndPositionBetweenOrderByPositionAsc
            (@Param("todo") Todo todo, @Param("parentTask") Task parentTask
                    , @Param("start") int firstPosition, @Param("start") int secondPosition);
}
