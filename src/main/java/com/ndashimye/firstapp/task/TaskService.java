package com.ndashimye.firstapp.task;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import java.util.List;

public interface TaskService {

    TaskDTO getTaskDTOById(Long taskId) throws AppEntityNotFoundException;

    //Service methods that handle all the operations related to tasks
    Task getTaskById(Long taskId) throws AppEntityNotFoundException;

    List<TaskDTO> getCompletedTasks(Long goalId) throws AppEntityNotFoundException;

    List<TaskDTO> getUncompletedTasks(Long goalId) throws AppEntityNotFoundException;

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
    related to the relationship between tasks and goals
    */
    void addNewTaskToGoal(TaskCreationDTO task, Long goalId)
            throws AppEntityNotFoundException;

    List<TaskDTO> getAllTasksByGoalId(Long goalId) throws AppEntityNotFoundException;

    List<TaskDTO> getLastTasksByGoalId(Long goalId) throws AppEntityNotFoundException;

    List<TaskDTO> getAllTasksByGoalIdOrderedByPriority(Long goalId)
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
