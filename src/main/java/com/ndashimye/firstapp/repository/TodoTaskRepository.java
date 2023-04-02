package com.ndashimye.firstapp.repository;

import com.ndashimye.firstapp.model.TodoTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoTaskRepository extends JpaRepository<TodoTask, Integer> {

}
