package com.ndashimye.firstapp.todo;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.error.InvalidTimeFormatException;
import com.ndashimye.firstapp.project.Project;
import java.util.List;

public interface TodoService {

    //Service methods that handle all the operations related to todos
    Todo getTodoById(Long todoId) throws AppEntityNotFoundException;

    void updateTodo(Todo updatedTodo, Long todoId) throws AppEntityNotFoundException;

    void deleteTodo(Long todoId)
            throws AppEntityNotFoundException;

    /*
    Service methods that handle all the operations
    related to the relationship between todos and projects
    */
    List<Todo> getAllTodosOfUserByProjectId(Long userId, Long projectId) throws AppEntityNotFoundException;

    void addNewTodoToProject(Todo todo, Long projectId)
            throws AppEntityNotFoundException;


    void moveTodoToProject(Long todoId, Long projectId)
            throws AppEntityNotFoundException;

    void assignPositionInProjectToNewTodo(Project project, Todo todo);

    void updateTodoPositionInProject(Long todoId, int newPosition)
            throws AppEntityNotFoundException;

    /*
    Service methods that handle all the operations
    related to the relationship between todos and users
    */
    List<Todo> getAllTodosByUserId(Long userId) throws AppEntityNotFoundException;

    List<Todo> getAllTodosByUserIdOrderedByPriority(Long userId)
            throws AppEntityNotFoundException;

    List<Todo> getAllTodosByUserIdOrderedByMostRecent(Long userId)
            throws AppEntityNotFoundException;

    List<Todo> getAllTodosByUserIdOrderedByLeastRecent(Long userId)
            throws AppEntityNotFoundException;

    List<Todo> getAllTodosByUserIdBetweenDates
            (Long userId, String start, String end)
            throws AppEntityNotFoundException, InvalidTimeFormatException;
}
