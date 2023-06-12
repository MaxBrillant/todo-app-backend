package com.ndashimye.firstapp.todo;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class TodoDTOMapper implements Function<Todo, TodoDTO> {
    @Override
    public TodoDTO apply(Todo todo) {
        return new TodoDTO(
                todo.getTodoId(),
                todo.getProject().getProjectId(),
                todo.getName(),
                todo.getDescription(),
                todo.getDueTime(),
                todo.getPriorityLevel(),
                todo.getPosition(),
                todo.getIsRecurrent(),
                todo.getCreatedAt(),
                todo.getUpdatedAt());
    }
}
