package com.ndashimye.firstapp.task;

import com.ndashimye.firstapp.todo.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {


    List<Task> findByTodoTask_TodoOrderByTodoTask_OrderAsc(Todo todo);

    List<Task> findByTodoTask_TodoOrderByTodoTask_PriorityLevelDesc(Todo todo);

    @Query("SELECT t FROM Task t " +
            "INNER JOIN TodoTask tt ON t.todoTask = tt " +
            "INNER JOIN Todo td ON tt.todo = td " +
            "WHERE td = :todo AND tt.isCompleted = true " +
            "ORDER BY tt.priorityLevel DESC")
    List<Task> findByCompletedTasks(@Param("todo") Todo todo);

    @Query("SELECT t FROM Task t " +
            "INNER JOIN TodoTask tt ON t.todoTask = tt " +
            "INNER JOIN Todo td ON tt.todo = td " +
            "WHERE td = :todo AND tt.isCompleted = false " +
            "ORDER BY tt.priorityLevel DESC")
    List<Task> findByUncompletedTasks(@Param("todo") Todo todo);




    List<Task> findByParentTaskOrderByTodoTask_OrderAsc(Task task);

    List<Task> findByParentTaskOrderByTodoTask_PriorityLevelDesc(Task task);

    @Query("SELECT t FROM Task t " +
            "INNER JOIN TodoTask tt ON t.todoTask = tt " +
            "WHERE t.parentTask = :task AND tt.isCompleted = true " +
            "ORDER BY tt.priorityLevel DESC")
    List<Task> findByCompletedChildTasks(@Param("task") Task Task);

    @Query("SELECT t FROM Task t " +
            "INNER JOIN TodoTask tt ON t.todoTask = tt " +
            "WHERE t.parentTask = :task AND tt.isCompleted = false " +
            "ORDER BY tt.priorityLevel DESC")
    List<Task> findByUncompletedChildTasks(@Param("task") Task task);
}
