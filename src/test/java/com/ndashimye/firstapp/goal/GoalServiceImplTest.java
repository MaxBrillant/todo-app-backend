package com.ndashimye.firstapp.goal;

import com.ndashimye.firstapp.task.Task;
import com.ndashimye.firstapp.task.TaskRepository;
import com.ndashimye.firstapp.user.User;
import com.ndashimye.firstapp.user.UserNotFoundException;
import com.ndashimye.firstapp.user.UserServiceImpl;
import com.ndashimye.firstapp.usersettings.UserSettings;
import com.ndashimye.firstapp.usersettings.UserSettingsNotFoundException;
import com.ndashimye.firstapp.blacklisteduser.BlacklistedUser;
import com.ndashimye.firstapp.blacklisteduser.UserTodoNotFoundException;
import com.ndashimye.firstapp.blacklisteduser.BlacklistedUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalServiceImplTest {
    @InjectMocks
    GoalServiceImpl goalServiceImpl;

    @Mock
    UserServiceImpl userServiceImpl;

    @Mock
    GoalRepository goalRepository;

    @Mock
    BlacklistedUserRepository blacklistedUserRepository;

    @Mock
    TaskRepository taskRepository;

    @Test
    void getTodoByIdTest() throws TodoNotFoundException {
        // Given
        Long todoId = 1L;
        Goal expectedGoal = new Goal();
        expectedGoal.setGoalId(todoId);

        when(goalRepository.findById(todoId)).thenReturn(Optional.of(expectedGoal));

        // When
        Goal actualGoal = goalServiceImpl.getGoalById(todoId);

        // Then
        assertEquals(expectedGoal, actualGoal);
        verify(goalRepository).findById(todoId);
    }

    @Test
    void givenInvalidTodoId_whenGetTodoById_thenThrowTodoNotFoundException() {
        // Given
        Long todoId = 2L;

        when(goalRepository.findById(todoId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(TodoNotFoundException.class, () -> goalServiceImpl.getGoalById(todoId));
        verify(goalRepository).findById(todoId);
    }


    @Test
    void getAllTasksByTodoIdTest() throws TodoNotFoundException {
        // Given
        Long todoId = 1L;
        Goal goal = new Goal();
        goal.setGoalId(todoId);
        List<Task> expectedTasks = new ArrayList<>();
        Task task1 = new Task();
        Task task2 = new Task();
        expectedTasks.add(task1);
        expectedTasks.add(task2);

        when(goalRepository.findById(todoId)).thenReturn(Optional.of(goal));
        when(taskRepository.findByTodoTask_TodoOrderByTodoTask_PositionAsc(goal)).thenReturn(expectedTasks);

        // When
        List<Task> actualTasks = goalServiceImpl.getAllTasksByTodoId(todoId);

        // Then
        assertEquals(expectedTasks, actualTasks);
        verify(goalRepository).findById(todoId);
        verify(taskRepository).findByTodoTask_TodoOrderByTodoTask_PositionAsc(goal);
    }

    @Test
    void getAllTasksByTodoOrderedByPriorityIdTest() throws TodoNotFoundException {
        // Given
        Long todoId = 1L;
        Goal goal = new Goal();
        goal.setGoalId(todoId);
        List<Task> expectedTasks = new ArrayList<>();
        Task task1 = new Task();
        Task task2 = new Task();
        expectedTasks.add(task1);
        expectedTasks.add(task2);

        when(goalRepository.findById(todoId)).thenReturn(Optional.of(goal));
        when(taskRepository.findByTodoTask_TodoOrderByTodoTask_PriorityLevelDesc(goal)).thenReturn(expectedTasks);

        // When
        List<Task> actualTasks = goalServiceImpl.getAllTasksByTodoIdOrderedByPriority(todoId);

        // Then
        assertEquals(expectedTasks, actualTasks);
        verify(goalRepository).findById(todoId);
        verify(taskRepository).findByTodoTask_TodoOrderByTodoTask_PriorityLevelDesc(goal);
    }


    @Test
    void getCompletedTasksTest() throws TodoNotFoundException {
        // Given
        Long todoId = 1L;
        Goal goal = new Goal();
        goal.setGoalId(todoId);
        List<Task> expectedCompletedTasks = new ArrayList<>();
        Task completedTask1 = new Task();
        Task completedTask2 = new Task();
        expectedCompletedTasks.add(completedTask1);
        expectedCompletedTasks.add(completedTask2);

        when(goalRepository.findById(todoId)).thenReturn(Optional.of(goal));
        when(taskRepository.findByCompletedTasks(goal)).thenReturn(expectedCompletedTasks);

        // When
        List<Task> actualCompletedTasks = goalServiceImpl.getCompletedTasks(todoId);

        // Then
        assertEquals(expectedCompletedTasks, actualCompletedTasks);
        verify(goalRepository).findById(todoId);
        verify(taskRepository).findByCompletedTasks(goal);
    }


    @Test
    void getUnCompletedTasksTest() throws TodoNotFoundException {
        // Given
        Long todoId = 1L;
        Goal goal = new Goal();
        goal.setGoalId(todoId);
        List<Task> expectedUnCompletedTasks = new ArrayList<>();
        Task UncompletedTask1 = new Task();
        Task UncompletedTask2 = new Task();
        expectedUnCompletedTasks.add(UncompletedTask1);
        expectedUnCompletedTasks.add(UncompletedTask2);

        when(goalRepository.findById(todoId)).thenReturn(Optional.of(goal));
        when(taskRepository.findByUncompletedTasks(goal)).thenReturn(expectedUnCompletedTasks);

        // When
        List<Task> actualCompletedTasks = goalServiceImpl.getUncompletedTasks(todoId);

        // Then
        assertEquals(expectedUnCompletedTasks, actualCompletedTasks);
        verify(goalRepository).findById(todoId);
        verify(taskRepository).findByUncompletedTasks(goal);
    }


    @Test
    void addNewTodoTest() throws UserNotFoundException, UserTodoNotFoundException {
        // Given
        Long userId = 1L;
        Goal goal = new Goal();
        goal.setGoalId(1L);
        User user = new User();
        user.setUserId(userId);

        when(userServiceImpl.getUserById(userId)).thenReturn(user);
        when(goalRepository.save(goal)).thenReturn(goal);

        // When
        goalServiceImpl.addNewTodo(goal, userId);

        // Then
        assertNotNull(goal.getUserTodo());
        verify(userServiceImpl).getUserById(userId);
        verify(goalRepository, times(2)).save(goal);
    }

    @Test
    void updateTodoTest()
            throws TodoNotFoundException, UserNotFoundException, UserTodoNotFoundException, UserSettingsNotFoundException {
        // Given
        Long todoId = 1L;
        Goal originalGoal = new Goal();
        originalGoal.setUserTodo(BlacklistedUser.builder().user(User.builder()
                .settings(UserSettings.builder().timeZone("UTC").build()).build()).build());
        originalGoal.setGoalId(todoId);
        originalGoal.setName("Original Name");
        originalGoal.setDescription("Original Description");
        originalGoal.setDueTime(ZonedDateTime.of(LocalDateTime.now().minusDays(3), ZoneId.of("UTC")));
        originalGoal.setIsRecurrent(false);

        Goal updatedGoal = new Goal();
        updatedGoal.setName("Updated Name");
        updatedGoal.setDescription("Updated Description");
        updatedGoal.setDueTime(ZonedDateTime.of(LocalDateTime.now().minusDays(7), ZoneId.of("UTC")));
        updatedGoal.setIsRecurrent(true);
        updatedGoal.setUserTodo(BlacklistedUser.builder().user(User.builder()
                .settings(UserSettings.builder().timeZone("UTC").build()).build()).build());

        when(goalRepository.findById(todoId)).thenReturn(Optional.of(originalGoal));
        when(goalRepository.save(originalGoal)).thenReturn(originalGoal);

        // When
        goalServiceImpl.updateGoal(updatedGoal, todoId);

        // Then
        assertEquals(updatedGoal.getName(), originalGoal.getName());
        assertEquals(updatedGoal.getDescription(), originalGoal.getDescription());
        assertEquals(updatedGoal.getDueTime(), originalGoal.getDueTime());
        assertEquals(updatedGoal.getIsRecurrent(), originalGoal.getIsRecurrent());
        verify(goalRepository).findById(todoId);
        verify(goalRepository).save(originalGoal);
    }


    @Test
    void deleteTodoTest() throws TodoNotFoundException, UserTodoNotFoundException {
        // Given
        Long todoId = 1L;
        Goal goal = new Goal();
        goal.setGoalId(todoId);
        goal.setUserTodo(BlacklistedUser.builder().build());

        when(goalRepository.findById(todoId)).thenReturn(Optional.of(goal)).thenReturn(Optional.empty());

        // When
        goalServiceImpl.deleteGoal(todoId);

        // Then
        verify(goalRepository).findById(todoId);
        verify(blacklistedUserRepository).delete(goal.getUserTodo());
        verify(goalRepository).delete(goal);

        Optional<Goal> deletedTodo = goalRepository.findById(todoId);
        assertFalse(deletedTodo.isPresent());
    }

    @Test
    void addNewUserTodoTest() throws UserNotFoundException, UserTodoNotFoundException {
        // Given
        Goal goal = new Goal();
        goal.setGoalId(1L);

        BlacklistedUser blackListedUser = new BlacklistedUser();
        blackListedUser.setUser(User.builder().userId(1L).username("testUser").build());


        // When
        when(blacklistedUserRepository.save(blackListedUser)).thenReturn(blackListedUser);
        goalServiceImpl.addNewUserTodo(goal, blackListedUser);

        // Then
        assertEquals(goal.getUserTodo(), blackListedUser);
        verify(blacklistedUserRepository).save(blackListedUser);
        //verify(mock(GoalService.class)).assignPositionToNewTodo(goal);
    }

    @Test
    void givenNoExistingTodos_whenAssignPositionToNewTodo_thenPositionIsOne() throws UserTodoNotFoundException {
        // Given

        Goal goal = new Goal();
        goal.setGoalId(1L);
        goal.setUserTodo(BlacklistedUser.builder().user(User.builder().build()).build());

        // When
        when(goalRepository.getMaxPosition()).thenReturn(null);

        goalServiceImpl.assignPositionToNewTodo(goal);

        // Then
        assertEquals(1, goal.getUserTodo().getPosition());
        verify(goalRepository).save(goal);
    }

    @Test
    void givenExistingTodos_whenAssignPositionToNewTodo_thenPositionIsMaxPlusOne() throws UserTodoNotFoundException {
        // Given

        Goal goal = new Goal();
        goal.setGoalId(1L);
        goal.setUserTodo(BlacklistedUser.builder().user(User.builder().build()).build());

        // When
        when(goalRepository.getMaxPosition()).thenReturn(3);


        goalServiceImpl.assignPositionToNewTodo(goal);

        // Then
        assertEquals(4, goal.getUserTodo().getPosition());
        verify(goalRepository).save(goal);
    }


    @Test
    void updateUserTodoTest() throws TodoNotFoundException, UserNotFoundException, UserTodoNotFoundException {
        // Given
        Long todoId = 1L;
        Goal goal = new Goal();
        goal.setGoalId(todoId);

        BlacklistedUser blackListedUser = BlacklistedUser.builder()
                .user(User.builder().userId(1L).username("testUser").build())
                .priorityLevel(5).build();

        goal.setUserTodo(blackListedUser);
        when(goalRepository.findById(todoId)).thenReturn(Optional.of(goal));

        BlacklistedUser updatedBlacklistedUser = new BlacklistedUser();
        updatedBlacklistedUser.setPriorityLevel(2);

        // When
        goalServiceImpl.updateUserTodo(todoId, updatedBlacklistedUser);

        // Then
        assertEquals(2, goal.getUserTodo().getPriorityLevel());
    }

    @Test
    public void deleteUserTodoTest() throws UserTodoNotFoundException {
        Long todoId = 1L;
        Goal goal = new Goal();
        goal.setGoalId(todoId);

        BlacklistedUser blackListedUser = BlacklistedUser.builder().build();
        goal.setUserTodo(blackListedUser);

        goalServiceImpl.deleteUserTodo(goal);

        verify(blacklistedUserRepository).delete(goal.getUserTodo());
    }


    @Test
    public void updateTodoPositionTest() throws TodoNotFoundException, UserTodoNotFoundException {

        Long todoId = 1L;
        Goal goal = new Goal();
        goal.setGoalId(todoId);

        BlacklistedUser blackListedUser = BlacklistedUser.builder().build();
        goal.setUserTodo(blackListedUser);

        int newPosition = 3;
        Goal goalToUpdate = new Goal();
        goalToUpdate.setGoalId(2L);
        BlacklistedUser blacklistedUserToUpdate = new BlacklistedUser();
        blacklistedUserToUpdate.setPosition(2);
        goalToUpdate.setUserTodo(blacklistedUserToUpdate);
        List<Goal> todosToUpdate = Arrays.asList(goalToUpdate);

        when(goalRepository.findById(goal.getGoalId())).thenReturn(Optional.of(goal));
        when(goalRepository.findTodosWithPositionsBetween(anyInt(), anyInt())).thenReturn(todosToUpdate);

        goalServiceImpl.updateTodoPosition(goal.getGoalId(), newPosition);

        verify(goalRepository).findTodosWithPositionsBetween(anyInt(), anyInt());
        verify(goalRepository, times(2)).save(any(Goal.class));
    }
}
