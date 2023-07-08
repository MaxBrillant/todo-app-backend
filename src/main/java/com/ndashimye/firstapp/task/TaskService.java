package com.ndashimye.firstapp.task;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.error.InvalidTimeFormatException;

import java.util.List;

public interface TaskService {

    TaskDTO getTaskDTOById(Long taskId) throws AppEntityNotFoundException;

    //Service methods that handle all the operations related to tasks
    Task getTaskById(Long taskId) throws AppEntityNotFoundException;

    List<TaskDTO> getCompletedTasksInProject(Long projectId) throws AppEntityNotFoundException;

    List<TaskDTO> getUncompletedTasksInProject(Long projectId) throws AppEntityNotFoundException;

    void updateTask(TaskCreationDTO updatedTask, Long taskId)
            throws AppEntityNotFoundException;

    void deleteTask(Long taskId)
            throws AppEntityNotFoundException;

    void assignTaskToUser(Long userId, Long taskId)
            throws AppEntityNotFoundException;

    void completeTask(Long taskId)
            throws AppEntityNotFoundException;

    void unCompleteTask(Long taskId) throws AppEntityNotFoundException;

    void assignPositionToNewTask(Task task) throws AppEntityNotFoundException;

    void updateTaskPosition(Long taskId, int newPosition)
            throws AppEntityNotFoundException;

    /*
    Service methods that handle all the operations
    related to the relationship between tasks and projects
    */
    void addNewTaskToProject(TaskCreationDTO task, Long projectId)
            throws AppEntityNotFoundException;

    void moveTaskToProject(Long taskId, Long projectId)
            throws AppEntityNotFoundException;

    List<TaskDTO> getAllTasksByProjectId(Long projectId) throws AppEntityNotFoundException;

    List<TaskDTO> getLastTasksByProjectId(Long projectId) throws AppEntityNotFoundException;

    List<TaskDTO> getAllTasksByProjectIdOrderedByPriority(Long projectId)
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

    List<TaskDTO> getAllTasksByUserId(Long userId)
            throws AppEntityNotFoundException;

    List<TaskDTO> getAllTasksByUserIdOrderedByMostRecent(Long userId)
            throws AppEntityNotFoundException;

    List<TaskDTO> getAllTasksByUserIdOrderedByLeastRecent(Long userId)
            throws AppEntityNotFoundException;

    List<TaskDTO> getAllTasksByUserIdBetweenDates
            (Long userId, String start, String end)
            throws AppEntityNotFoundException, InvalidTimeFormatException;
}
