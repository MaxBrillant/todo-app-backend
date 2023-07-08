package com.ndashimye.firstapp.task;

import com.ndashimye.firstapp.ZonedDateTimeAttributeConverter;
import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.error.InvalidTimeFormatException;
import com.ndashimye.firstapp.project.Project;
import com.ndashimye.firstapp.project.ProjectService;
import com.ndashimye.firstapp.user.User;
import com.ndashimye.firstapp.user.UserService;
import com.ndashimye.firstapp.userproject.ProjectRole;
import com.ndashimye.firstapp.userproject.UserProject;
import com.ndashimye.firstapp.userproject.UserProjectService;
import com.ndashimye.firstapp.usersettings.UserSettings;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final UserService userService;
    private final ProjectService projectService;
    private final UserProjectService userProjectService;
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
    public List<TaskDTO> getCompletedTasksInProject(Long projectId)
            throws AppEntityNotFoundException {

        Project project = projectService.getProjectById(projectId);
        log.info("Fetching all completed tasks of project of ID: {}...", project);
        List<Task> tasks = taskRepository.findByCompletedTasks(project);
        log.info("All completed tasks of project of ID: {} were successfully fetched.", projectId);

        return tasks.stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getUncompletedTasksInProject(Long projectId)
            throws AppEntityNotFoundException {

        Project project = projectService.getProjectById(projectId);
        log.info("Fetching all uncompleted tasks of project of ID: {}...", projectId);
        List<Task> tasks = taskRepository.findByUncompletedTasks(project);
        log.info("All uncompleted tasks of project of ID: {} were successfully fetched.", projectId);

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
        task.setDescription(updatedTask.description());
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
    public void assignTaskToUser(Long userId, Long taskId)
            throws AppEntityNotFoundException {

        //TODO: get the authenticated user

        Task task = getTaskById(taskId);
        UserProject userProject = userProjectService.getUserProjectByUserIdAndProjectId(
                userId, task.getProject().getProjectId());

        log.info("Assigning task of ID: {} to user of ID: {}...",
                taskId, userId);

        task.setAssignedToUser(userProject);

        log.info("Task of ID: {} was successfully assigned to user of ID: {} and username: {}."
                , task.getTaskId(), userProject.getUser().getUserId(),
                userProject.getUser().getUsername());
    }

    @Override
    public void completeTask(Long taskId)
            throws AppEntityNotFoundException {

        //TODO: get the authenticated user
        Long userId = 1L;

        Task task = getTaskById(taskId);
        UserProject userProject = userProjectService.getUserProjectByUserIdAndProjectId(
                userId, task.getProject().getProjectId());

        boolean canCompleteTask = false;

        if(userProject.getProjectRole().equals(ProjectRole.CREATOR) ||
            userProject.getProjectRole().equals(ProjectRole.ADMIN)) {
            canCompleteTask = true;
        }else {
        if (task.getAssignedToUser().equals(userProject) || task.getAssignedToUser() == null) {
                canCompleteTask = true;
            }
        }

        if (canCompleteTask) {
            log.info("Completing the task...");
            task.setCompletedByUser(userProject);
            task.setCompletionTime(ZonedDateTime.of(LocalDateTime.now()
                    , ZoneId.of(userProject.getUser().getSettings().getTimeZone())));

            log.info("Task of ID: {} was successfully completed by user of ID: {} and username: {}."
                    , task.getTaskId(), userProject.getUser().getUserId(),
                    userProject.getUser().getUsername());
        }
    }

    @Override
    public void unCompleteTask(Long taskId) throws AppEntityNotFoundException {

        //TODO: get the authenticated user
        Long userId = 1L;

        Task task = getTaskById(taskId);
        UserProject userProject = userProjectService.getUserProjectByUserIdAndProjectId(
                userId, task.getProject().getProjectId());

        boolean canUncompleteTask = false;

        if(userProject.getProjectRole().equals(ProjectRole.CREATOR) ||
                userProject.getProjectRole().equals(ProjectRole.ADMIN)) {
            canUncompleteTask = true;
        }else {
            if (task.getAssignedToUser().equals(userProject) || task.getAssignedToUser() == null) {
                canUncompleteTask = true;
            }
        }

        if (canUncompleteTask) {
            log.info("Uncompleting the task...");
            task.setCompletedByUser(null);
            task.setCompletionTime(null);
            log.info("Task of ID: {} was successfully uncompleted."
                    , task.getTaskId());
        }
    }

    @Override
    public void assignPositionToNewTask(Task task) {

        log.info("Calculating the maximum position value...");

        Integer maxPosition = task.getParentTask() == null?
                taskRepository.getMaxPositionOfTasksWithNoParentTasks(task.getProject()):
                taskRepository.getMaxPositionOfTasksWithParentTasks
                        (task.getProject(), task.getParentTask());

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
        Project project = task.getProject();

        log.info("Getting the current position of task of ID: {}...", task.getTaskId());
        int currentPosition = task.getPosition();

        if (newPosition > currentPosition) {

            List<Task> tasksToUpdate =
                    task.getParentTask() == null?
                            taskRepository.findByProjectAndPositionWithNoParentTaskBetweenOrderByPositionAsc
                                (project, currentPosition + 1, newPosition):
                            taskRepository.findByProjectAndPositionWithParentTaskBetweenOrderByPositionAsc
                                (project, task.getParentTask(), currentPosition + 1, newPosition);

            log.info("Updating positions of tasks that are between position {} and {}", currentPosition, newPosition);
            for (Task taskToUpdate : tasksToUpdate) {
                taskToUpdate.setPosition(taskToUpdate.getPosition() - 1);
            }
            log.info("The positions of all tasks that are between position {} and {} were successfully updated."
                    , currentPosition, newPosition);

        } else if (newPosition < currentPosition) {

            List<Task> tasksToUpdate =
                    task.getParentTask() == null?
                            taskRepository.findByProjectAndPositionWithNoParentTaskBetweenOrderByPositionAsc
                                    (project, newPosition, currentPosition - 1):
                            taskRepository.findByProjectAndPositionWithParentTaskBetweenOrderByPositionAsc
                                    (project, task.getParentTask(), newPosition, currentPosition - 1);

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
    related to the relationship between tasks and projects

    */

    @Override
    public void addNewTaskToProject(TaskCreationDTO taskCreationDTO, Long projectId)
            throws AppEntityNotFoundException {

        Project project = projectService.getProjectById(projectId);
        log.info("Adding a new task to project of ID: {}...", projectId);

        Task task = Task.builder()
                .project(project)
                .parentTask(taskCreationDTO.parentTaskId()==null?null:getTaskById(taskCreationDTO.parentTaskId()))
                .name(taskCreationDTO.name())
                .description(taskCreationDTO.description())
                .dueTime(taskCreationDTO.dueTime())
                .isRecurrent(taskCreationDTO.isRecurrent())
                .priorityLevel(taskCreationDTO.priorityLevel())
                .build();

        assignPositionToNewTask(task);

        taskRepository.save(task);

        log.info("Task of ID: {} was successfully added to project of ID: {}."
                , task.getTaskId(), project.getProjectId());
    }

    @Override
    public void moveTaskToProject(Long taskId, Long projectId)
            throws AppEntityNotFoundException {

        Task task = getTaskById(taskId);
        Project project = projectService.getProjectById(projectId);
        log.info("Moving task of ID: {} to project of ID: {}..."
                , taskId, projectId);

        task.setProject(project);
        task.setParentTask(null);
        log.info("Task of ID: {} was successfully moved to project of ID: {}."
                , taskId, projectId);
    }

    @Override
    public List<TaskDTO> getAllTasksByProjectId(Long projectId) throws AppEntityNotFoundException {

        Project project = projectService.getProjectById(projectId);
        log.info("Fetching all tasks of project of ID: {}...", projectId);
        List<Task> tasks = taskRepository.findByProjectAndOrderByPositionAsc(project);
        log.info("All tasks of project of ID: {} were successfully fetched.", project);

        return tasks.stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getLastTasksByProjectId(Long projectId) throws AppEntityNotFoundException {

        Project project = projectService.getProjectById(projectId);
        log.info("Fetching the last tasks of project of ID: {}...", projectId);
        List<Task> tasks = taskRepository.findByProjectAndOrderByPositionDesc(project);
        log.info("The last tasks of project of ID: {} were successfully fetched.", projectId);

        return tasks.stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getAllTasksByProjectIdOrderedByPriority(Long projectId)
            throws AppEntityNotFoundException {

        Project project = projectService.getProjectById(projectId);
        log.info("Fetching all tasks of project of ID: {} ordered by priority...", projectId);
        List<Task> tasks = taskRepository.findByProjectAndOrderByPriorityLevelDesc(project);
        log.info("All tasks of project of ID: {} ordered by priority were successfully fetched.", projectId);

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



    /*

    Service methods that handle all the operations
    related to the relationship between tasks and users

    */

    @Override
    public List<TaskDTO> getAllTasksByUserId(Long userId)
            throws AppEntityNotFoundException {

        User user = userService.getUserById(userId);
        log.info("Fetching all tasks of user of ID: {} and username: {}..."
                , user.getUserId(), user.getUsername());

        List<Task> tasks = taskRepository.findTasksOfUserAndOrderByPositionAsc(user);
        log.info("All tasks of user of ID: {} and username: {} were " +
                "successfully fetched.", user.getUserId(), user.getUsername());

        return tasks.stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getAllTasksByUserIdOrderedByMostRecent(Long userId)
            throws AppEntityNotFoundException {

        User user = userService.getUserById(userId);
        log.info("Fetching all tasks of user of ID: {} and username: {} ordered from most to least recent..."
                , user.getUserId(), user.getUsername());

        List<Task> tasks = taskRepository.findTasksOfUserAndOrderByDueTimeDesc(user);
        log.info("All tasks of user of ID: {} and username: {} ordered from most to " +
                "least recent were successfully fetched.", user.getUserId(), user.getUsername());

        return tasks.stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getAllTasksByUserIdOrderedByLeastRecent(Long userId)
            throws AppEntityNotFoundException {

        User user = userService.getUserById(userId);
        log.info("Fetching all tasks of user of ID: {} and username: {} ordered from least to most recent..."
                , user.getUserId(), user.getUsername());

        List<Task> tasks = taskRepository.findTasksOfUserAndOrderByDueTimeAsc(user);
        log.info("All tasks of user of ID: {} and username: {} ordered from least to " +
                "most recent were successfully fetched.", user.getUserId(), user.getUsername());

        return tasks.stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getAllTasksByUserIdBetweenDates
            (Long userId, String start, String end)
            throws AppEntityNotFoundException, InvalidTimeFormatException {


        LocalDate startDate;
        LocalDate endDate;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            startDate = LocalDate.parse(start, formatter);
            endDate = LocalDate.parse(end, formatter);
        } catch (Exception e) {
            throw new InvalidTimeFormatException();
        }

        User user = userService.getUserById(userId);

        log.info("Getting the timezone information from the user settings...");

        UserSettings userSettings = user.getSettings();

        ZoneId zoneId = ZoneId.of(userSettings.getTimeZone()); // or specify a specific timezone


        log.info("Successfully accessed the user's timezone information.");

        ZonedDateTime zonedStartDate = startDate.atStartOfDay(zoneId);
        ZonedDateTime zonedEndDate = endDate.plusDays(1).atStartOfDay(zoneId);


        log.info("Fetching all tasks of user of ID: {} and username: {} between {} and {}..."
                , user.getUserId(), user.getUsername(), start, end);

        List<Task> tasks = taskRepository.findTasksOfUserAndDueTimeBetween(user.getUserId(),
                Timestamp.valueOf(ZonedDateTimeAttributeConverter.toUtcZoneId(zonedStartDate).toLocalDateTime()),
                Timestamp.valueOf(ZonedDateTimeAttributeConverter.toUtcZoneId(zonedEndDate).toLocalDateTime()));

        log.info("All tasks of user of ID: {} and username: {} between {} and {} " +
                "were successfully fetched.", user.getUserId(), user.getUsername(), start, end);

        return tasks.stream()
                .map(taskDTOMapper)
                .collect(Collectors.toList());
    }
}
