package com.ndashimye.firstapp.todotask;

import com.ndashimye.firstapp.todo.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoTaskRepository extends JpaRepository<TodoTask, Integer> {

    TodoTask findTopByTodoOrderByPositionDesc(Todo todo);

    List<TodoTask> findByTodoAndPositionBetweenOrderByPositionAsc(Todo todo, int i, int newPosition);

    List<TodoTask> findByTodoAndPositionBetweenOrderByPositionDesc(Todo todo, int newPosition, int i);
}
