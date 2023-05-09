package com.ndashimye.firstapp.todoproject;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoProjectRepository extends JpaRepository<TodoProject, Long> {
}
