package com.ndashimye.firstapp.controller;

import com.ndashimye.firstapp.error.UserTodoNotFoundException;
import com.ndashimye.firstapp.model.UserTodo;
import com.ndashimye.firstapp.service.UserTodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user-todos")
public class UserTodoController {

    @Autowired
    private UserTodoService userTodoService;

    @GetMapping("/{userTodoId}")
    public UserTodo getUserTodoById(@PathVariable Integer userTodoId)
            throws UserTodoNotFoundException {

        return userTodoService.getUserTodoById(userTodoId);
    }

    @PostMapping()
    public String addUserTodo(@RequestBody UserTodo userTodo) {

        userTodoService.addNewUserTodo(userTodo);

        return "user todo of id "+userTodo.getUserTodoId()+" was added successfully";
    }

    @PutMapping("/{userTodoId}")
    public String updateUserTodo(@RequestBody UserTodo updatedUserTodo,
                                 @PathVariable Integer userTodoId) throws UserTodoNotFoundException {

        UserTodo userTodo = userTodoService.getUserTodoById(userTodoId);

        userTodoService.updateUserTodo(updatedUserTodo, userTodo);

        return "user todo of id "+userTodo.getUserTodoId()+" was updated successfully";
    }

    @DeleteMapping("/{userTodoId}")
    public String deleteUserTodo(@PathVariable Integer userTodoId) throws UserTodoNotFoundException {

        UserTodo userTodo = userTodoService.getUserTodoById(userTodoId);
        int id = userTodo.getUserTodoId();
        userTodoService.deleteUserTodo(userTodo);
        return "user todo of id "+id+" was successfully deleted from the database";
    }
}
