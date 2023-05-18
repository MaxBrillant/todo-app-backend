package com.ndashimye.firstapp.project;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.todo.Todo;
import com.ndashimye.firstapp.todo.TodoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/projects")
@AllArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final TodoService todoService;



    //HTTP endpoints that handle all the operations related to projects

    @PutMapping("/{projectId}")
    public void updateProject(@PathVariable Long projectId,
                              @RequestBody Project updatedProject)
            throws AppEntityNotFoundException {

        projectService.updateProject(projectId, updatedProject);
    }

    @DeleteMapping("/{projectId}")
    public void deleteProject(@PathVariable Long projectId)
            throws AppEntityNotFoundException {
        projectService.deleteProject(projectId);
    }



    /*

    HTTP endpoints that handle all the operations
    related to the relationship between projects and todos

    */

    @PostMapping("/{projectId}/todos")
    public void addTodoToProject(@RequestBody Todo todo, @PathVariable Long projectId)
            throws AppEntityNotFoundException {

        todoService.addNewTodoToProject(todo, projectId);
    }
}
