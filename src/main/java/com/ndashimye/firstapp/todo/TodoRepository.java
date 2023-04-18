package com.ndashimye.firstapp.todo;

import com.ndashimye.firstapp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer> {

    List<Todo> findByUserTodo_UserOrderByUserTodo_PositionAsc(User user);

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
            "AND t.due_time BETWEEN :startDate AND :endDate " +
            "ORDER BY ut.position ASC",
            nativeQuery = true)
    List<Todo> findByUserAndDueTimeBetween(Integer userId, Timestamp startDate, Timestamp endDate);


    @Query("SELECT MAX(userTodo.position) FROM Todo")
    Integer getMaxPosition();

    @Query("SELECT t FROM Todo t WHERE t.userTodo.position BETWEEN :start AND :end ORDER BY t.userTodo.position")
    List<Todo> findTodosWithPositionsBetween(@Param("start") int start, @Param("end") int end);
}
