package com.ndashimye.firstapp.todo;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.error.InvalidTimeFormatException;
import com.ndashimye.firstapp.project.Project;
import java.util.List;

public interface TodoService {

    //Service methods that handle all the operations related to todos
    TodoDTO getTodoDTOById(Long todoId) throws AppEntityNotFoundException;

    Todo getTodoById(Long todoId) throws AppEntityNotFoundException;

    void updateTodo(TodoCreationDTO updatedTodo, Long todoId)
            throws AppEntityNotFoundException;

    void deleteTodo(Long todoId)
            throws AppEntityNotFoundException;

    /*
    Service methods that handle all the operations
    related to the relationship between todos and projects
    */
    List<TodoDTO> getAllTodosOfUserByProjectId(Long userId, Long projectId)
            throws AppEntityNotFoundException;

    void addNewTodoToProject(TodoCreationDTO todoCreationDTO, Long projectId)
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
    List<TodoDTO> getAllTodosByUserId(Long userId) throws AppEntityNotFoundException;

    List<TodoDTO> getAllTodosByUserIdOrderedByPriority(Long userId)
            throws AppEntityNotFoundException;

    List<TodoDTO> getAllTodosByUserIdOrderedByMostRecent(Long userId)
            throws AppEntityNotFoundException;

    List<TodoDTO> getAllTodosByUserIdOrderedByLeastRecent(Long userId)
            throws AppEntityNotFoundException;

    List<TodoDTO> getAllTodosByUserIdBetweenDates
            (Long userId, String start, String end)
            throws AppEntityNotFoundException, InvalidTimeFormatException;
}
