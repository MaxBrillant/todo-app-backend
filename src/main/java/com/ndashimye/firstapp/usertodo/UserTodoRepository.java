package com.ndashimye.firstapp.usertodo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTodoRepository extends JpaRepository<UserTodo, Integer> {

}
