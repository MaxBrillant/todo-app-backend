package com.ndashimye.firstapp.task;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.goal.Goal;
import com.ndashimye.firstapp.goal.GoalService;
import com.ndashimye.firstapp.user.User;
import com.ndashimye.firstapp.user.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final UserService userService;
    private final GoalService goalService;
    private final TaskDTOMapper taskDTOMapper;
    private TaskRepository taskRepository;



    //Service methods that handle all the operations related to tasks

    @Override
    public TaskDTO getTaskDTOById(Long taskId) throws AppEntityNotFoundException {
        log.info("Fetching task by ID: {}...", taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AppEntityNotFoundException(Task.class));
        log.info("Task of ID: {} was successfully fetched.", taskId);

        return taskDTOMapper.apply(task);
    }

    @Override
    public Task getTaskById(Long taskId) throws AppEntityNotFoundException {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new AppEntityNotFoundException(Task.class));
    }

    @Override
    public List<TaskDTO> getCompletedTasks(Long goalId)
            throws AppEntityNotFoundException {

        Goal goal = goalService.getGoalById(goalId);
        log.info("Fetching all completed tasks of goal of ID: {}...", goalId);
        List<Task> tasks = taskRepository.findByCompletedTasks(goal);
        log.info("All completed tasks of goal of ID: {} were successfully fetched.", goalId);

        return tasks.stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getUncompletedTasks(Long goalId)
            throws AppEntityNotFoundException {

        Goal goal = goalService.getGoalById(goalId);
        log.info("Fetching all uncompleted tasks of goal of ID: {}...", goalId);
        List<Task> tasks = taskRepository.findByUncompletedTasks(goal);
        log.info("All uncompleted tasks of goal of ID: {} were successfully fetched.", goalId);

        return tasks.stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public void updateTask(TaskCreationDTO updatedTask, Long taskId)
            throws AppEntityNotFoundException {

        Task task = getTaskById(taskId);
        log.info("Updating task of ID: {}...", task.getTaskId());

        task.setName(updatedTask.name());
        task.setDueTime(updatedTask.dueTime());
        task.setIsRecurrent(updatedTask.isRecurrent());
        task.setPriorityLevel(updatedTask.priorityLevel());

        taskRepository.save(task);
        log.info("Task of ID: {} was successfully updated.", task.getTaskId());
    }

    @Override
    public void deleteTask(Long taskId)
            throws AppEntityNotFoundException {

        Task task = getTaskById(taskId);
        log.info("Deleting task of ID: {}...", task.getTaskId());
        Task deletedTask = task;
        taskRepository.delete(task);
        log.info("Task of ID: {} was successfully deleted.", deletedTask.getTaskId());
    }

    @Override
    public void completeTask(Long userId, Long taskId)
            throws AppEntityNotFoundException {

        Task task = getTaskById(taskId);
        User user = userService.getUserById(userId);

        log.info("Completing the task...");
        task.setCompletedByUser(user);
        task.setCompletionTime(ZonedDateTime.of(LocalDateTime.now()
                , ZoneId.of(user.getSettings().getTimeZone())));
        log.info("Task of ID: {} was successfully completed by user of ID: {} and username: {}."
                , task.getTaskId(), user.getUserId(), user.getUsername());
    }

    @Override
    public void unCompleteTask(Long taskId) throws AppEntityNotFoundException {
        Task task = getTaskById(taskId);

        log.info("Uncompleting the task...");
        task.setCompletedByUser(null);
        task.setCompletionTime(null);
        log.info("Task of ID: {} was successfully uncompleted."
                , task.getTaskId());

    }

    @Override
    public void assignPositionToNewTask(Task task) {

        log.info("Calculating the maximum position value...");

        Integer maxPosition = task.getParentTask() == null?
                taskRepository.getMaxPositionOfTasksWithNoParentTasks(task.getGoal()):
                taskRepository.getMaxPositionOfTasksWithParentTasks
                        (task.getGoal(), task.getParentTask());

        log.info("Assigning a position to task of ID: {}...", task.getTaskId());
        if (maxPosition == null) {
            maxPosition = 0;
        }

        task.setPosition(maxPosition + 1);
        log.info("Position of value: {} was successfully assigned to the new task of ID: {}."
                , task.getPosition(), task.getTaskId());
    }

    @Override
    public void updateTaskPosition(Long taskId, int newPosition)
            throws AppEntityNotFoundException {

        Task task = getTaskById(taskId);
        Goal goal = task.getGoal();

        log.info("Getting the current position of task of ID: {}...", task.getTaskId());
        int currentPosition = task.getPosition();

        if (newPosition > currentPosition) {

            List<Task> tasksToUpdate =
                    task.getParentTask() == null?
                            taskRepository.findByGoalAndPositionWithNoParentTaskBetweenOrderByPositionAsc
                                (goal, currentPosition + 1, newPosition):
                            taskRepository.findByGoalAndPositionWithParentTaskBetweenOrderByPositionAsc
                                (goal, task.getParentTask(), currentPosition + 1, newPosition);

            log.info("Updating positions of tasks that are between position {} and {}", currentPosition, newPosition);
            for (Task taskToUpdate : tasksToUpdate) {
                taskToUpdate.setPosition(taskToUpdate.getPosition() - 1);
            }
            log.info("The positions of all tasks that are between position {} and {} were successfully updated."
                    , currentPosition, newPosition);

        } else if (newPosition < currentPosition) {

            List<Task> tasksToUpdate =
                    task.getParentTask() == null?
                            taskRepository.findByGoalAndPositionWithNoParentTaskBetweenOrderByPositionAsc
                                    (goal, newPosition, currentPosition - 1):
                            taskRepository.findByGoalAndPositionWithParentTaskBetweenOrderByPositionAsc
                                    (goal, task.getParentTask(), newPosition, currentPosition - 1);

            log.info("Updating positions of tasks that are between position {} and {}", newPosition, currentPosition);
            for (Task taskToUpdate : tasksToUpdate) {
                taskToUpdate.setPosition(taskToUpdate.getPosition() + 1);
            }
            log.info("The positions of all tasks that are between position {} and {} were successfully updated."
                    , newPosition, currentPosition);
        }

        log.info("Updating the position of task of ID: {}...", task.getTaskId());
        task.setPosition(newPosition);
        log.info("The position of task of ID: {} was successfully updated.", task.getTaskId());
    }


    /*

    Service methods that handle all the operations
    related to the relationship between tasks and goals

    */

    @Override
    public void addNewTaskToGoal(TaskCreationDTO taskCreationDTO, Long goalId)
            throws AppEntityNotFoundException {

        Goal goal = goalService.getGoalById(goalId);
        log.info("Adding a new task to goal of ID: {}...", goal.getGoalId());

        Task task = Task.builder()
                .goal(goal)
                .parentTask(taskCreationDTO.parentTaskId()==null?null:getTaskById(taskCreationDTO.parentTaskId()))
                .name(taskCreationDTO.name())
                .dueTime(taskCreationDTO.dueTime())
                .isRecurrent(taskCreationDTO.isRecurrent())
                .priorityLevel(taskCreationDTO.priorityLevel())
                .build();

        assignPositionToNewTask(task);

        taskRepository.save(task);

        log.info("Task of ID: {} was successfully added to goal of ID: {}."
                , task.getTaskId(), goal.getGoalId());
    }

    @Override
    public List<TaskDTO> getAllTasksByGoalId(Long goalId) throws AppEntityNotFoundException {

        Goal goal = goalService.getGoalById(goalId);
        log.info("Fetching all tasks of goal of ID: {}...", goalId);
        List<Task> tasks = taskRepository.findByGoalAndOrderByPositionAsc(goal);
        log.info("All tasks of goal of ID: {} were successfully fetched.", goalId);

        return tasks.stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getLastTasksByGoalId(Long goalId) throws AppEntityNotFoundException {

        Goal goal = goalService.getGoalById(goalId);
        log.info("Fetching the last tasks of goal of ID: {}...", goalId);
        List<Task> tasks = taskRepository.findByGoalAndOrderByPositionDesc(goal);
        log.info("The last tasks of goal of ID: {} were successfully fetched.", goalId);

        return tasks.stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getAllTasksByGoalIdOrderedByPriority(Long goalId)
            throws AppEntityNotFoundException {

        Goal goal = goalService.getGoalById(goalId);
        log.info("Fetching all tasks of goal of ID: {} ordered by priority...", goalId);
        List<Task> tasks = taskRepository.findByGoalAndOrderByPriorityLevelDesc(goal);
        log.info("All tasks of goal of ID: {} ordered by priority were successfully fetched.", goalId);

        return tasks.stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }



    /*

    Service methods that handle all the operations
    related to the relationship between tasks and their children tasks (sub-tasks)

    */

    @Override
    public List<TaskDTO> getAllChildTasksByTaskId(Long taskId)
            throws AppEntityNotFoundException {

        Task task = getTaskById(taskId);
        log.info("Fetching all child tasks of task of ID: {}...", taskId);
        List<Task> tasks = taskRepository.findByParentTaskAndOrderByPositionAsc(task);
        log.info("All child tasks of task of ID: {} were successfully fetched.", taskId);

        return tasks.stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getLastChildTasksByTaskId(Long taskId)
            throws AppEntityNotFoundException {

        Task task = getTaskById(taskId);
        log.info("Fetching the last child tasks of task of ID: {}...", taskId);
        List<Task> tasks = taskRepository.findByParentTaskAndOrderByPositionDesc(task);
        log.info("The last child tasks of task of ID: {} were successfully fetched.", taskId);

        return tasks.stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getAllChildTasksByTaskIdOrderedByPriority(Long taskId)
            throws AppEntityNotFoundException {

        Task task = getTaskById(taskId);
        log.info("Fetching all child tasks of task of ID: {} ordered by priority...", taskId);
        List<Task> tasks = taskRepository.findByParentTaskAndOrderByPriorityLevelDesc(task);
        log.info("All child tasks of task of ID: {} ordered by priority were successfully fetched.", taskId);

        return tasks.stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getCompletedChildTasks(Long taskId)
            throws AppEntityNotFoundException {

        Task task = getTaskById(taskId);
        log.info("Fetching all completed child tasks of task of ID: {}...", taskId);
        List<Task> tasks = taskRepository.findByCompletedChildTasks(task);
        log.info("All completed child tasks of task of ID: {} were successfully fetched.", taskId);

        return tasks.stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getUncompletedChildTasks(Long taskId)
            throws AppEntityNotFoundException {

        Task task = getTaskById(taskId);
        log.info("Fetching all uncompleted child tasks of task of ID: {}...", taskId);
        List<Task> tasks = taskRepository.findByUncompletedChildTasks(task);
        log.info("All uncompleted child tasks of task of ID: {} were successfully fetched.", taskId);

        return tasks.stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }
}
