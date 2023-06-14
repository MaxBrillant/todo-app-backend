package com.ndashimye.firstapp.task;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import java.util.List;

public interface TaskService {

    TaskDTO getTaskDTOById(Long taskId) throws AppEntityNotFoundException;

    //Service methods that handle all the operations related to tasks
    Task getTaskById(Long taskId) throws AppEntityNotFoundException;

    List<TaskDTO> getCompletedTasks(Long todoId) throws AppEntityNotFoundException;

    List<TaskDTO> getUncompletedTasks(Long todoId) throws AppEntityNotFoundException;

    void updateTask(TaskCreationDTO updatedTask, Long taskId)
            throws AppEntityNotFoundException;

    void deleteTask(Long taskId)
            throws AppEntityNotFoundException;

    void completeTask(Long taskId, Long userId)
            throws AppEntityNotFoundException;

    void unCompleteTask(Long taskId) throws AppEntityNotFoundException;

    void assignPositionToNewTask(Task task) throws AppEntityNotFoundException;

    void updateTaskPosition(Long taskId, int newPosition)
            throws AppEntityNotFoundException;

    /*
    Service methods that handle all the operations
    related to the relationship between tasks and todos
    */
    void addNewTaskToTodo(TaskCreationDTO task, Long todoId)
            throws AppEntityNotFoundException;

    List<TaskDTO> getAllTasksByTodoId(Long todoId) throws AppEntityNotFoundException;

    List<TaskDTO> getLastTasksByTodoId(Long todoId) throws AppEntityNotFoundException;

    List<TaskDTO> getAllTasksByTodoIdOrderedByPriority(Long todoId)
            throws AppEntityNotFoundException;

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
    List<TaskDTO> getAllChildTasksByTaskId(Long taskId)
            throws AppEntityNotFoundException;

    List<TaskDTO> getLastChildTasksByTaskId(Long taskId)
            throws AppEntityNotFoundException;

    List<TaskDTO> getAllChildTasksByTaskIdOrderedByPriority(Long taskId)
            throws AppEntityNotFoundException;

    List<TaskDTO> getCompletedChildTasks(Long taskId) throws AppEntityNotFoundException;

    List<TaskDTO> getUncompletedChildTasks(Long taskId) throws AppEntityNotFoundException;
}
