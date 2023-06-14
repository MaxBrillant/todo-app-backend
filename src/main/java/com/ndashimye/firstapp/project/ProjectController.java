package com.ndashimye.firstapp.project;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.todo.TodoCreationDTO;
import com.ndashimye.firstapp.todo.TodoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/projects")
@AllArgsConstructor
public class ProjectController {
    private final TodoService todoService;



    /*

    HTTP endpoints that handle all the operations
    related to the relationship between projects and todos

    */

    @PostMapping("/{projectId}/todos")
    public void addTodoToProject(@RequestBody TodoCreationDTO todoCreationDTO, @PathVariable Long projectId)
            throws AppEntityNotFoundException {

        todoService.addNewTodoToProject(todoCreationDTO, projectId);
    }
}
