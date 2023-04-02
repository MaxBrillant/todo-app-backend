package com.ndashimye.firstapp.repository;

import com.ndashimye.firstapp.model.Todo;
import com.ndashimye.firstapp.model.User;
import com.ndashimye.firstapp.model.UserTodo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer> {

    //List<UserTodo> findByUserOrderByOrderAsc(User user);

    List<Todo> findByUserTodo_UserOrderByUserTodo_OrderAsc(User user);

    List<Todo> findByUserTodo_UserOrderByUserTodo_PriorityLevelDesc(User user);

    @Query("SELECT t from Todo t " +
            "INNER JOIN UserTodo ut on t.userTodo = ut " +
            "ORDER BY t.dueTime DESC")
    List<Todo> findByUserOrderByDueTimeDesc(User user);

    @Query("SELECT t from Todo t " +
            "INNER JOIN UserTodo ut on t.userTodo = ut " +
            "ORDER BY t.dueTime ASC")
    List<Todo> findByUserOrderByDueTimeAsc(User user);

    @Query(value = "SELECT t.* FROM todo t " +
            "INNER JOIN user_todo ut ON ut.user_todo_id = t.user_todo_id " +
            "WHERE ut.user_id = :userId " +
            "AND t.due_time BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    List<Todo> findByUserAndDueTimeBetween(Integer userId, Timestamp startDate, Timestamp endDate);
}
