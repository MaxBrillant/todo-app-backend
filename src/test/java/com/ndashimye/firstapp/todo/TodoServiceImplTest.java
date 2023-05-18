package com.ndashimye.firstapp.todo;

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
class TodoServiceImplTest {
    @InjectMocks
    TodoServiceImpl todoServiceImpl;

    @Mock
    UserServiceImpl userServiceImpl;

    @Mock
    TodoRepository todoRepository;

    @Mock
    BlacklistedUserRepository blacklistedUserRepository;

    @Mock
    TaskRepository taskRepository;

    @Test
    void getTodoByIdTest() throws TodoNotFoundException {
        // Given
        Long todoId = 1L;
        Todo expectedTodo = new Todo();
        expectedTodo.setTodoId(todoId);

        when(todoRepository.findById(todoId)).thenReturn(Optional.of(expectedTodo));

        // When
        Todo actualTodo = todoServiceImpl.getTodoById(todoId);

        // Then
        assertEquals(expectedTodo, actualTodo);
        verify(todoRepository).findById(todoId);
    }

    @Test
    void givenInvalidTodoId_whenGetTodoById_thenThrowTodoNotFoundException() {
        // Given
        Long todoId = 2L;

        when(todoRepository.findById(todoId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(TodoNotFoundException.class, () -> todoServiceImpl.getTodoById(todoId));
        verify(todoRepository).findById(todoId);
    }


    @Test
    void getAllTasksByTodoIdTest() throws TodoNotFoundException {
        // Given
        Long todoId = 1L;
        Todo todo = new Todo();
        todo.setTodoId(todoId);
        List<Task> expectedTasks = new ArrayList<>();
        Task task1 = new Task();
        Task task2 = new Task();
        expectedTasks.add(task1);
        expectedTasks.add(task2);

        when(todoRepository.findById(todoId)).thenReturn(Optional.of(todo));
        when(taskRepository.findByTodoTask_TodoOrderByTodoTask_PositionAsc(todo)).thenReturn(expectedTasks);

        // When
        List<Task> actualTasks = todoServiceImpl.getAllTasksByTodoId(todoId);

        // Then
        assertEquals(expectedTasks, actualTasks);
        verify(todoRepository).findById(todoId);
        verify(taskRepository).findByTodoTask_TodoOrderByTodoTask_PositionAsc(todo);
    }

    @Test
    void getAllTasksByTodoOrderedByPriorityIdTest() throws TodoNotFoundException {
        // Given
        Long todoId = 1L;
        Todo todo = new Todo();
        todo.setTodoId(todoId);
        List<Task> expectedTasks = new ArrayList<>();
        Task task1 = new Task();
        Task task2 = new Task();
        expectedTasks.add(task1);
        expectedTasks.add(task2);

        when(todoRepository.findById(todoId)).thenReturn(Optional.of(todo));
        when(taskRepository.findByTodoTask_TodoOrderByTodoTask_PriorityLevelDesc(todo)).thenReturn(expectedTasks);

        // When
        List<Task> actualTasks = todoServiceImpl.getAllTasksByTodoIdOrderedByPriority(todoId);

        // Then
        assertEquals(expectedTasks, actualTasks);
        verify(todoRepository).findById(todoId);
        verify(taskRepository).findByTodoTask_TodoOrderByTodoTask_PriorityLevelDesc(todo);
    }


    @Test
    void getCompletedTasksTest() throws TodoNotFoundException {
        // Given
        Long todoId = 1L;
        Todo todo = new Todo();
        todo.setTodoId(todoId);
        List<Task> expectedCompletedTasks = new ArrayList<>();
        Task completedTask1 = new Task();
        Task completedTask2 = new Task();
        expectedCompletedTasks.add(completedTask1);
        expectedCompletedTasks.add(completedTask2);

        when(todoRepository.findById(todoId)).thenReturn(Optional.of(todo));
        when(taskRepository.findByCompletedTasks(todo)).thenReturn(expectedCompletedTasks);

        // When
        List<Task> actualCompletedTasks = todoServiceImpl.getCompletedTasks(todoId);

        // Then
        assertEquals(expectedCompletedTasks, actualCompletedTasks);
        verify(todoRepository).findById(todoId);
        verify(taskRepository).findByCompletedTasks(todo);
    }


    @Test
    void getUnCompletedTasksTest() throws TodoNotFoundException {
        // Given
        Long todoId = 1L;
        Todo todo = new Todo();
        todo.setTodoId(todoId);
        List<Task> expectedUnCompletedTasks = new ArrayList<>();
        Task UncompletedTask1 = new Task();
        Task UncompletedTask2 = new Task();
        expectedUnCompletedTasks.add(UncompletedTask1);
        expectedUnCompletedTasks.add(UncompletedTask2);

        when(todoRepository.findById(todoId)).thenReturn(Optional.of(todo));
        when(taskRepository.findByUncompletedTasks(todo)).thenReturn(expectedUnCompletedTasks);

        // When
        List<Task> actualCompletedTasks = todoServiceImpl.getUncompletedTasks(todoId);

        // Then
        assertEquals(expectedUnCompletedTasks, actualCompletedTasks);
        verify(todoRepository).findById(todoId);
        verify(taskRepository).findByUncompletedTasks(todo);
    }


    @Test
    void addNewTodoTest() throws UserNotFoundException, UserTodoNotFoundException {
        // Given
        Long userId = 1L;
        Todo todo = new Todo();
        todo.setTodoId(1L);
        User user = new User();
        user.setUserId(userId);

        when(userServiceImpl.getUserById(userId)).thenReturn(user);
        when(todoRepository.save(todo)).thenReturn(todo);

        // When
        todoServiceImpl.addNewTodo(todo, userId);

        // Then
        assertNotNull(todo.getUserTodo());
        verify(userServiceImpl).getUserById(userId);
        verify(todoRepository, times(2)).save(todo);
    }

    @Test
    void updateTodoTest()
            throws TodoNotFoundException, UserNotFoundException, UserTodoNotFoundException, UserSettingsNotFoundException {
        // Given
        Long todoId = 1L;
        Todo originalTodo = new Todo();
        originalTodo.setUserTodo(BlacklistedUser.builder().user(User.builder()
                .settings(UserSettings.builder().timeZone("UTC").build()).build()).build());
        originalTodo.setTodoId(todoId);
        originalTodo.setName("Original Name");
        originalTodo.setDescription("Original Description");
        originalTodo.setDueTime(ZonedDateTime.of(LocalDateTime.now().minusDays(3), ZoneId.of("UTC")));
        originalTodo.setIsRecurrent(false);

        Todo updatedTodo = new Todo();
        updatedTodo.setName("Updated Name");
        updatedTodo.setDescription("Updated Description");
        updatedTodo.setDueTime(ZonedDateTime.of(LocalDateTime.now().minusDays(7), ZoneId.of("UTC")));
        updatedTodo.setIsRecurrent(true);
        updatedTodo.setUserTodo(BlacklistedUser.builder().user(User.builder()
                .settings(UserSettings.builder().timeZone("UTC").build()).build()).build());

        when(todoRepository.findById(todoId)).thenReturn(Optional.of(originalTodo));
        when(todoRepository.save(originalTodo)).thenReturn(originalTodo);

        // When
        todoServiceImpl.updateTodo(updatedTodo, todoId);

        // Then
        assertEquals(updatedTodo.getName(), originalTodo.getName());
        assertEquals(updatedTodo.getDescription(), originalTodo.getDescription());
        assertEquals(updatedTodo.getDueTime(), originalTodo.getDueTime());
        assertEquals(updatedTodo.getIsRecurrent(), originalTodo.getIsRecurrent());
        verify(todoRepository).findById(todoId);
        verify(todoRepository).save(originalTodo);
    }


    @Test
    void deleteTodoTest() throws TodoNotFoundException, UserTodoNotFoundException {
        // Given
        Long todoId = 1L;
        Todo todo = new Todo();
        todo.setTodoId(todoId);
        todo.setUserTodo(BlacklistedUser.builder().build());

        when(todoRepository.findById(todoId)).thenReturn(Optional.of(todo)).thenReturn(Optional.empty());

        // When
        todoServiceImpl.deleteTodo(todoId);

        // Then
        verify(todoRepository).findById(todoId);
        verify(blacklistedUserRepository).delete(todo.getUserTodo());
        verify(todoRepository).delete(todo);

        Optional<Todo> deletedTodo = todoRepository.findById(todoId);
        assertFalse(deletedTodo.isPresent());
    }

    @Test
    void addNewUserTodoTest() throws UserNotFoundException, UserTodoNotFoundException {
        // Given
        Todo todo = new Todo();
        todo.setTodoId(1L);

        BlacklistedUser blackListedUser = new BlacklistedUser();
        blackListedUser.setUser(User.builder().userId(1L).username("testUser").build());


        // When
        when(blacklistedUserRepository.save(blackListedUser)).thenReturn(blackListedUser);
        todoServiceImpl.addNewUserTodo(todo, blackListedUser);

        // Then
        assertEquals(todo.getUserTodo(), blackListedUser);
        verify(blacklistedUserRepository).save(blackListedUser);
        //verify(mock(TodoService.class)).assignPositionToNewTodo(todo);
    }

    @Test
    void givenNoExistingTodos_whenAssignPositionToNewTodo_thenPositionIsOne() throws UserTodoNotFoundException {
        // Given

        Todo todo = new Todo();
        todo.setTodoId(1L);
        todo.setUserTodo(BlacklistedUser.builder().user(User.builder().build()).build());

        // When
        when(todoRepository.getMaxPosition()).thenReturn(null);

        todoServiceImpl.assignPositionToNewTodo(todo);

        // Then
        assertEquals(1, todo.getUserTodo().getPosition());
        verify(todoRepository).save(todo);
    }

    @Test
    void givenExistingTodos_whenAssignPositionToNewTodo_thenPositionIsMaxPlusOne() throws UserTodoNotFoundException {
        // Given

        Todo todo = new Todo();
        todo.setTodoId(1L);
        todo.setUserTodo(BlacklistedUser.builder().user(User.builder().build()).build());

        // When
        when(todoRepository.getMaxPosition()).thenReturn(3);


        todoServiceImpl.assignPositionToNewTodo(todo);

        // Then
        assertEquals(4, todo.getUserTodo().getPosition());
        verify(todoRepository).save(todo);
    }


    @Test
    void updateUserTodoTest() throws TodoNotFoundException, UserNotFoundException, UserTodoNotFoundException {
        // Given
        Long todoId = 1L;
        Todo todo = new Todo();
        todo.setTodoId(todoId);

        BlacklistedUser blackListedUser = BlacklistedUser.builder()
                .user(User.builder().userId(1L).username("testUser").build())
                .priorityLevel(5).build();

        todo.setUserTodo(blackListedUser);
        when(todoRepository.findById(todoId)).thenReturn(Optional.of(todo));

        BlacklistedUser updatedBlacklistedUser = new BlacklistedUser();
        updatedBlacklistedUser.setPriorityLevel(2);

        // When
        todoServiceImpl.updateUserTodo(todoId, updatedBlacklistedUser);

        // Then
        assertEquals(2, todo.getUserTodo().getPriorityLevel());
    }

    @Test
    public void deleteUserTodoTest() throws UserTodoNotFoundException {
        Long todoId = 1L;
        Todo todo = new Todo();
        todo.setTodoId(todoId);

        BlacklistedUser blackListedUser = BlacklistedUser.builder().build();
        todo.setUserTodo(blackListedUser);

        todoServiceImpl.deleteUserTodo(todo);

        verify(blacklistedUserRepository).delete(todo.getUserTodo());
    }


    @Test
    public void updateTodoPositionTest() throws TodoNotFoundException, UserTodoNotFoundException {

        Long todoId = 1L;
        Todo todo = new Todo();
        todo.setTodoId(todoId);

        BlacklistedUser blackListedUser = BlacklistedUser.builder().build();
        todo.setUserTodo(blackListedUser);

        int newPosition = 3;
        Todo todoToUpdate = new Todo();
        todoToUpdate.setTodoId(2L);
        BlacklistedUser blacklistedUserToUpdate = new BlacklistedUser();
        blacklistedUserToUpdate.setPosition(2);
        todoToUpdate.setUserTodo(blacklistedUserToUpdate);
        List<Todo> todosToUpdate = Arrays.asList(todoToUpdate);

        when(todoRepository.findById(todo.getTodoId())).thenReturn(Optional.of(todo));
        when(todoRepository.findTodosWithPositionsBetween(anyInt(), anyInt())).thenReturn(todosToUpdate);

        todoServiceImpl.updateTodoPosition(todo.getTodoId(), newPosition);

        verify(todoRepository).findTodosWithPositionsBetween(anyInt(), anyInt());
        verify(todoRepository, times(2)).save(any(Todo.class));
    }
}
