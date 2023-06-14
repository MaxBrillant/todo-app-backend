package com.ndashimye.firstapp.todo;

import com.ndashimye.firstapp.ZonedDateTimeAttributeConverter;
import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.error.InvalidTimeFormatException;
import com.ndashimye.firstapp.project.Project;
import com.ndashimye.firstapp.project.ProjectService;
import com.ndashimye.firstapp.user.User;
import com.ndashimye.firstapp.user.UserService;
import com.ndashimye.firstapp.usersettings.UserSettings;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final UserService userService;
    private final ProjectService projectService;
    private final TodoDTOMapper todoDTOMapper;
    private TodoRepository todoRepository;



    //Service methods that handle all the operations related to todos

    @Override
    public TodoDTO getTodoDTOById(Long todoId) throws AppEntityNotFoundException {
        log.info("Fetching todo by ID: {}...", todoId);
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new AppEntityNotFoundException(Todo.class));
        log.info("Todo of ID: {} was successfully fetched.", todoId);

        return todoDTOMapper.apply(todo);
    }

    @Override
    public Todo getTodoById(Long todoId) throws AppEntityNotFoundException {
        return todoRepository.findById(todoId)
                .orElseThrow(() -> new AppEntityNotFoundException(Todo.class));
    }

    @Override
    public void updateTodo(TodoCreationDTO updatedTodo, Long todoId)
            throws AppEntityNotFoundException {

        Todo todo = getTodoById(todoId);
        log.info("Updating todo of ID: {}...", todo.getTodoId());

        todo.setName(updatedTodo.name());
        todo.setDescription(updatedTodo.description());
        todo.setDueTime(updatedTodo.dueTime());
        todo.setPriorityLevel(updatedTodo.priorityLevel());
        todo.setIsRecurrent(updatedTodo.isRecurrent());

        todoRepository.save(todo);
        log.info("Todo of ID: {} was successfully updated.", todo.getTodoId());
    }

    @Override
    public void deleteTodo(Long todoId)
            throws AppEntityNotFoundException {

        Todo todo = getTodoById(todoId);
        log.info("Deleting todo of ID: {}...", todo.getTodoId());

        Todo deletedTodo = todo;
        todoRepository.delete(todo);
        log.info("Todo of ID: {} was successfully deleted.", deletedTodo.getTodoId());
    }



    /*

    Service methods that handle all the operations
    related to the relationship between todos and projects

    */

    @Override
    public List<TodoDTO> getAllTodosOfUserByProjectId(Long userId, Long projectId)
            throws AppEntityNotFoundException {

        User user = userService.getUserById(userId);
        Project project = projectService.getProjectById(projectId);
        log.info("Fetching all todos of project of ID: {}...", projectId);
        List<Todo> todos = todoRepository.findByProjectAndUserAndOrderByPositionAsc(user, project);
        log.info("All todos of project of ID: {} were successfully fetched.", project.getProjectId());

        return todos.stream()
                .map(todoDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoDTO> getLastTodosOfUserByProjectId(Long userId, Long projectId)
            throws AppEntityNotFoundException {

        User user = userService.getUserById(userId);
        Project project = projectService.getProjectById(projectId);
        log.info("Fetching the last todos of project of ID: {}...", projectId);
        List<Todo> todos = todoRepository.findByProjectAndUserAndOrderByPositionDesc(user, project);
        log.info("The last todos of project of ID: {} were successfully fetched.", project.getProjectId());

        return todos.stream()
                .map(todoDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public void addNewTodoToProject(TodoCreationDTO todoCreationDTO, Long projectId)
            throws AppEntityNotFoundException {

        log.info("Adding a new todo to project of ID: {}...", projectId);

        //Checking if the project exists and assigning it to the new todo
        Project project = projectService.getProjectById(projectId);
        Todo todo = Todo.builder()
                .name(todoCreationDTO.name())
                .description(todoCreationDTO.description())
                .dueTime(todoCreationDTO.dueTime())
                .priorityLevel(todoCreationDTO.priorityLevel())
                .isRecurrent(todoCreationDTO.isRecurrent())
                .build();

        todo.setProject(project);

        //Assign position to new todo
        assignPositionInProjectToNewTodo(project, todo);

        todoRepository.save(todo);
        log.info("Todo of ID: {} was successfully added to project of ID: {}."
                , todo.getTodoId(), project.getProjectId());
    }

    @Override
    public void moveTodoToProject(Long todoId, Long projectId)
            throws AppEntityNotFoundException {

        Todo todo = getTodoById(todoId);
        Project project = projectService.getProjectById(projectId);
        log.info("Moving todo of ID: {} to project of ID: {}..."
                , todo.getTodoId(), project.getProjectId());

        todo.setProject(project);
        todoRepository.save(todo);
        log.info("Todo of ID: {} was successfully moved to project of ID: {}."
                , todo.getTodoId(), project.getProjectId());
    }

    @Override
    public void assignPositionInProjectToNewTodo(Project project, Todo todo) {

        log.info("Calculating the maximum position value of all existing todos...");
        // Get the maximum position value from all existing todos
        Integer maxPosition = todoRepository.getMaxPosition(project);

        // If there are no existing todos, set the position to 1
        if (maxPosition == null) {
            maxPosition = 0;
        }

        log.info("Assigning a position to a new todo of ID: {}...", todo.getTodoId());
        // Assign the new todo's position to be the maximum position + 1
        todo.setPosition(maxPosition + 1);

        // Save the new todo
        todoRepository.save(todo);
        log.info("Position of value: {} was successfully assigned to the new todo of ID: {}."
                , todo.getPosition(), todo.getTodoId());
    }

    @Override
    public void updateTodoPositionInProject(Long todoId, int newPosition)
            throws AppEntityNotFoundException {

        Todo todo = getTodoById(todoId);

        log.info("Getting the current position of todo of ID: {}...", todo.getTodoId());
        // Get the current position of the todo
        int currentPosition = todo.getPosition();

        // If the new position is equal to the current position, do nothing
        if (newPosition == currentPosition) {
            return;
        }

        log.info("Getting all todos with positions between the current ({}) and new ({}) positions..."
                , currentPosition, newPosition);

        // Get the todos with positions between the current and new positions
        List<Todo> todosToUpdate;
        if (newPosition > currentPosition) {
            todosToUpdate = todoRepository.findAccessibleTodosOfUserWithPositionsBetween
                    (todo.getProject(), currentPosition + 1, newPosition);
        } else {
            todosToUpdate = todoRepository.findAccessibleTodosOfUserWithPositionsBetween
                    (todo.getProject(), newPosition, currentPosition - 1);
        }

        log.info("Updating positions of all the todos that are between position {} and {}...", currentPosition, newPosition);
        // Update the positions of the affected todos
        for (Todo todoToUpdate : todosToUpdate) {
            if (newPosition > currentPosition) {
                todoToUpdate.setPosition(todoToUpdate.getPosition() - 1);
            } else {
                todoToUpdate.setPosition(todoToUpdate.getPosition() + 1);
            }
            todoRepository.save(todoToUpdate);
        }
        log.info("The positions of all todos that are between position {} and {} were successfully updated."
                , currentPosition, newPosition);

        log.info("Updating the position of todo of ID: {}...", todo.getTodoId());
        // Update the position of the target todo
        todo.setPosition(newPosition);
        todoRepository.save(todo);
        log.info("The position of todo of ID: {} was successfully updated.", todo.getTodoId());
    }



    /*

    Service methods that handle all the operations
    related to the relationship between todos and users

    */

    @Override
    public List<TodoDTO> getAllTodosByUserId(Long userId)
            throws AppEntityNotFoundException {

        User user = userService.getUserById(userId);
        log.info("Fetching all todos of user of ID: {} and username: {}..."
                , user.getUserId(), user.getUsername());

        List<Todo> todos = todoRepository.findAccessibleTodosOfUserAndOrderByPositionAsc(user);
        log.info("All todos of user of ID: {} and username: {} were " +
                "successfully fetched.", user.getUserId(), user.getUsername());

        return todos.stream()
                .map(todoDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoDTO> getAllTodosByUserIdOrderedByPriority(Long userId)
            throws AppEntityNotFoundException {

        User user = userService.getUserById(userId);
        log.info("Fetching all todos of user of ID: {} and username: {} ordered by priority..."
                , user.getUserId(), user.getUsername());

        List<Todo> todos = todoRepository.findAccessibleTodosOfUserAndOrderByPriorityLevelDesc(user);
        log.info("All todos of user of ID: {} and username: {} ordered by priority " +
                "were successfully fetched.", user.getUserId(), user.getUsername());

        return todos.stream()
                .map(todoDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoDTO> getAllTodosByUserIdOrderedByMostRecent(Long userId)
            throws AppEntityNotFoundException {

        User user = userService.getUserById(userId);
        log.info("Fetching all todos of user of ID: {} and username: {} ordered from most to least recent..."
                , user.getUserId(), user.getUsername());

        List<Todo> todos = todoRepository.findAccessibleTodosOfUserAndOrderByDueTimeDesc(user);
        log.info("All todos of user of ID: {} and username: {} ordered from most to " +
                "least recent were successfully fetched.", user.getUserId(), user.getUsername());

        return todos.stream()
                .map(todoDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoDTO> getAllTodosByUserIdOrderedByLeastRecent(Long userId)
            throws AppEntityNotFoundException {

        User user = userService.getUserById(userId);
        log.info("Fetching all todos of user of ID: {} and username: {} ordered from least to most recent..."
                , user.getUserId(), user.getUsername());

        List<Todo> todos = todoRepository.findAccessibleTodosOfUserAndOrderByDueTimeAsc(user);
        log.info("All todos of user of ID: {} and username: {} ordered from least to " +
                "most recent were successfully fetched.", user.getUserId(), user.getUsername());

        return todos.stream()
                .map(todoDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<TodoDTO> getAllTodosByUserIdBetweenDates
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


        log.info("Fetching all todos of user of ID: {} and username: {} between {} and {}..."
                , user.getUserId(), user.getUsername(), start, end);

        List<Todo> todos = todoRepository.findAccessibleTodosOfUserAndDueTimeBetween(user.getUserId(),
                Timestamp.valueOf(ZonedDateTimeAttributeConverter.toUtcZoneId(zonedStartDate).toLocalDateTime()),
                Timestamp.valueOf(ZonedDateTimeAttributeConverter.toUtcZoneId(zonedEndDate).toLocalDateTime()));

        log.info("All todos of user of ID: {} and username: {} between {} and {} " +
                "were successfully fetched.", user.getUserId(), user.getUsername(), start, end);

        return todos.stream()
                .map(todoDTOMapper)
                .collect(Collectors.toList());
    }
}
