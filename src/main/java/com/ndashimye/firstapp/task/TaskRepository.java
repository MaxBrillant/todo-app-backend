package com.ndashimye.firstapp.task;
import com.ndashimye.firstapp.todo.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
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

    @Query(value = "SELECT t.* FROM task t " +
            "INNER JOIN today_task tt ON tt.task_id = t.task_id " +
            "INNER JOIN todo_task tot ON t.todo_task_id = tot.todo_task_id " +
            "INNER JOIN todo todo ON tot.todo_id = todo.todo_id " +
            "INNER JOIN user_todo ut ON ut.user_todo_id = todo.user_todo_id " +
            "WHERE ut.user_id = :userId AND tt.date BETWEEN :date AND :nextDate",
            nativeQuery = true)
    List<Task> findAllByDateAndUser(@Param("userId") int userId, @Param("date") Timestamp date,
                                    @Param("nextDate") Timestamp nextDate);
}
