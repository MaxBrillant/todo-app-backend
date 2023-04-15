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
            "WHERE td = :todo AND tt.isCompleted = true ")
    List<Task> findByCompletedTasks(Todo todo);

    @Query("SELECT t FROM Task t " +
            "INNER JOIN TodoTask tt ON t.todoTask = tt " +
            "INNER JOIN Todo td ON tt.todo = td " +
            "WHERE td = :todo AND tt.isCompleted = false")
    List<Task> findByUncompletedTasks(@Param("todo") Todo todo);
}
