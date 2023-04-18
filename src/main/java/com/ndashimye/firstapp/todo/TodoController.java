package com.ndashimye.firstapp.todo;

import com.ndashimye.firstapp.task.Task;
import com.ndashimye.firstapp.task.TaskNotFoundException;
import com.ndashimye.firstapp.usertodo.UserTodo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/{todoId}")
    public Todo getTodoById(@PathVariable Integer todoId) throws TodoNotFoundException {
        return todoService.getTodoById(todoId);
    }


    @GetMapping("/id/{todoId}/tasks")
    public List<Task> getTasksByTodoId(@PathVariable Integer todoId) throws TodoNotFoundException {
        return todoService.getAllTasksByTodoId(todoId);
    }

    @GetMapping("/id/{todoId}/tasks/order-by/priority")
    public List<Task> getTasksByTodoIdOrderedByPriority(@PathVariable Integer todoId)
            throws TodoNotFoundException {

        return todoService.getAllTasksByTodoIdOrderedByPriority(todoId);
    }

    @GetMapping("/id/{todoId}/tasks/completed")
    public List<Task> getCompletedTasks(@PathVariable Integer todoId) throws TodoNotFoundException {
        return todoService.getCompletedTasks(todoId);
    }

    @GetMapping("/id/{todoId}/tasks/uncompleted")
    public List<Task> getUncompletedTasks(@PathVariable Integer todoId) throws TodoNotFoundException {
        return todoService.getUncompletedTasks(todoId);
    }

    @PostMapping()
    public String addTodo(@RequestBody Todo todo) {

        todoService.addNewTodo(todo);

        return "todo of id "+todo.getTodoId()+" was added successfully";
    }

    @PutMapping("/{todoId}")
    public String updateTodo(@RequestBody Todo updatedTodo, @PathVariable Integer todoId)
            throws TodoNotFoundException {

        Todo todo = todoService.getTodoById(todoId);

        todoService.updateTodo(updatedTodo, todo);

        return "todo of id "+todo.getTodoId()+" was updated successfully";
    }

    @DeleteMapping("/{todoId}")
    public String deleteTodo(@PathVariable Integer todoId) throws TodoNotFoundException {

        Todo todo = todoService.getTodoById(todoId);
        int id = todo.getTodoId();
        todoService.deleteTodo(todo);

        return "todo of id "+id+" was successfully deleted from the database";
    }



    @PostMapping("/{todoId}/user-todo")
    public String addUserTodo(@PathVariable Integer todoId,
                              @RequestBody UserTodo userTodo)
            throws TodoNotFoundException {

        todoService.addNewUserTodo(todoId, userTodo);

        return "user todo of id "+userTodo.getUserTodoId()+" was added successfully";
    }

    @PutMapping("/{todoId}/user-todo")
    public String updateUserTodo(@PathVariable Integer todoId,
                                 @RequestBody UserTodo updatedUserTodo)
            throws TodoNotFoundException {

        todoService.updateUserTodo(todoId, updatedUserTodo);

        return "user todo of id "+updatedUserTodo.getUserTodoId()+" was updated successfully";
    }


    @PutMapping("/{todoId}/user-todo/update")
    public String updateUserTodoPosition(@PathVariable Integer todoId,
                                         @RequestParam Integer position)
            throws TodoNotFoundException {

        todoService.updateTodoPosition(todoId, position);

        return "position of user todo that belongs to the todo of id "+todoId+" was updated successfully";
    }

    @DeleteMapping("/{todoId}/user-todo")
    public String deleteUserTodo(@PathVariable Integer todoId) throws TodoNotFoundException {

        Todo todo = todoService.getTodoById(todoId);
        int id = todo.getUserTodo().getUserTodoId();
        todoService.deleteUserTodo(todoId);

        return "user todo of id "+id+" was successfully deleted from the database";
    }
}
