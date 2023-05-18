package com.ndashimye.firstapp.task;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import java.util.List;

public interface TaskService {

    //Service methods that handle all the operations related to tasks
    Task getTaskById(Long taskId) throws AppEntityNotFoundException;

    List<Task> getCompletedTasks(Long todoId) throws AppEntityNotFoundException;

    List<Task> getUncompletedTasks(Long todoId) throws AppEntityNotFoundException;

    void updateTask(Task updatedTask, Long taskId) throws AppEntityNotFoundException;

    void deleteTask(Long taskId)
            throws AppEntityNotFoundException;

    void completeTask(Long taskId, Long userId) throws AppEntityNotFoundException;

    void unCompleteTask(Long taskId) throws AppEntityNotFoundException;

    void assignPositionToNewTask(Task task) throws AppEntityNotFoundException;

    void updateTaskPosition(Long taskId, int newPosition)
            throws AppEntityNotFoundException;

    /*
    Service methods that handle all the operations
    related to the relationship between tasks and todos
    */
    void addNewTaskToTodo(Task task, Long todoId)
            throws AppEntityNotFoundException;

    List<Task> getAllTasksByTodoId(Long todoId) throws AppEntityNotFoundException;

    List<Task> getAllTasksByTodoIdOrderedByPriority(Long todoId) throws AppEntityNotFoundException;

    /*
    Service methods that handle all the operations
    related to the relationship between tasks and their parent tasks
    */
    void updateParentTask(Long taskId, Long parentTaskId, int position)
            throws AppEntityNotFoundException;

    /*
    Service methods that handle all the operations
    related to the relationship between tasks and their children tasks (sub-tasks)
    */
    List<Task> getAllChildTasksByTaskId(Long taskId) throws AppEntityNotFoundException;

    List<Task> getAllChildTasksByTaskIdOrderedByPriority(Long taskId) throws AppEntityNotFoundException;

    List<Task> getCompletedChildTasks(Long taskId) throws AppEntityNotFoundException;

    List<Task> getUncompletedChildTasks(Long taskId) throws AppEntityNotFoundException;
}
