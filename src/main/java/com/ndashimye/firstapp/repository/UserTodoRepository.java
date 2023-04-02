package com.ndashimye.firstapp.repository;

import com.ndashimye.firstapp.model.Todo;
import com.ndashimye.firstapp.model.User;
import com.ndashimye.firstapp.model.UserTodo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserTodoRepository extends JpaRepository<UserTodo, Integer> {

}
