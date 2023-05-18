package com.ndashimye.firstapp.todo;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.task.Task;
import com.ndashimye.firstapp.task.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/todos")
@AllArgsConstructor
public class TodoController {

    private final TodoService todoService;
    private final TaskService taskService;



    /*

    HTTP endpoints that handle all the operations related to todos

    */

    @PutMapping("/{todoId}")
    public void updateTodo(@PathVariable Long todoId, @RequestBody Todo updatedTodo)
            throws AppEntityNotFoundException {

        todoService.updateTodo(updatedTodo, todoId);
    }

    @PutMapping("/{todoId}/update/position")
    public void updateTodoPosition(@PathVariable Long todoId,
                                   @RequestParam Integer position)
            throws AppEntityNotFoundException {

        todoService.updateTodoPositionInProject(todoId, position);
    }

    @DeleteMapping("/{todoId}")
    public void deleteTodo(@PathVariable Long todoId)
            throws AppEntityNotFoundException {

        todoService.deleteTodo(todoId);
    }

    @GetMapping("/{todoId}")
    public Todo getTodoById(@PathVariable Long todoId) throws AppEntityNotFoundException {
        return todoService.getTodoById(todoId);
    }



    /*

    HTTP endpoints that handle all the operations
    related to the relationship between todos and projects

    */

    @PutMapping("/{todoId}/move-to/{projectId}")
    public void moveToProject(@PathVariable Long todoId,
                              @PathVariable Long projectId)
            throws AppEntityNotFoundException {

        todoService.moveTodoToProject(todoId, projectId);
    }



    /*

    HTTP endpoints that handle all the operations
    related to the relationship between todos and tasks

    */

    @PostMapping("/{todoId}/tasks")
    public void addTaskToTodo(@RequestBody Task task, @PathVariable Long todoId)
            throws AppEntityNotFoundException {

        taskService.addNewTaskToTodo(task, todoId);
    }

    @GetMapping("/{todoId}/tasks")
    public List<Task> getTasksByTodoId(@PathVariable Long todoId) throws AppEntityNotFoundException {
        return taskService.getAllTasksByTodoId(todoId);
    }

    @GetMapping("/{todoId}/tasks/order-by/priority")
    public List<Task> getTasksByTodoIdOrderedByPriority(@PathVariable Long todoId)
            throws AppEntityNotFoundException {

        return taskService.getAllTasksByTodoIdOrderedByPriority(todoId);
    }

    @GetMapping("/{todoId}/tasks/completed")
    public List<Task> getCompletedTasks(@PathVariable Long todoId) throws AppEntityNotFoundException {
        return taskService.getCompletedTasks(todoId);
    }

    @GetMapping("/{todoId}/tasks/uncompleted")
    public List<Task> getUncompletedTasks(@PathVariable Long todoId) throws AppEntityNotFoundException {
        return taskService.getUncompletedTasks(todoId);
    }
}
