package com.ndashimye.firstapp.todoGeneration;

import com.ndashimye.firstapp.todo.TodoDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class GeneratedTodoDTOMapper implements Function<TodoDTO, GeneratedTodoDTO> {
    @Override
    public GeneratedTodoDTO apply(TodoDTO todoDTO) {
        return new GeneratedTodoDTO(
                todoDTO.projectId(),
                todoDTO.name(),
                todoDTO.description());
    }
}
