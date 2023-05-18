package com.ndashimye.firstapp.blacklisteduser;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.todo.Todo;
import java.util.List;

public interface BlackListedUserService {

    /*
    Service methods that handle all the operations
    related to the relationship between users and their restricted todos
    */
    List<Todo> getRestrictedTodosOfUserInProject(Long projectId, Long userId)
            throws AppEntityNotFoundException;

    void restrictUserFromAccessingTodoInProject(Long userId, Long projectId, Long todoId)
            throws AppEntityNotFoundException;
}
