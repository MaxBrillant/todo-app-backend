package com.ndashimye.firstapp.todoGeneration;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/users/{userId}/projects/{projectId}/generate/todos")
public class TodoGenerationController {
    private final TodoGenerationService todoGenerationService;

    @GetMapping()
    public List<GeneratedTodoDTO> generateTodosOfProject(@PathVariable Long userId,
                                                         @PathVariable Long projectId,
                                                         @RequestParam("number") Integer numberOfTodos)
            throws AppEntityNotFoundException, IllegalAccessException {

        return todoGenerationService.generateTodosOfProject(userId, projectId, numberOfTodos);
    }
}
