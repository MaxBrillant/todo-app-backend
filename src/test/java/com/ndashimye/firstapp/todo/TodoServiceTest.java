package com.ndashimye.firstapp.todo;

import com.ndashimye.firstapp.task.Task;
import com.ndashimye.firstapp.task.TaskRepository;
import com.ndashimye.firstapp.user.User;
import com.ndashimye.firstapp.user.UserNotFoundException;
import com.ndashimye.firstapp.user.UserRepository;
import com.ndashimye.firstapp.user.UserService;
import com.ndashimye.firstapp.usersettings.UserSettings;
import com.ndashimye.firstapp.usersettings.UserSettingsNotFoundException;
import com.ndashimye.firstapp.usertodo.UserTodo;
import com.ndashimye.firstapp.usertodo.UserTodoNotFoundException;
import com.ndashimye.firstapp.usertodo.UserTodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
class TodoServiceTest {
    @InjectMocks
    TodoService todoService;

    @Mock
    UserService userService;

    @Mock
    TodoRepository todoRepository;

    @Mock
    UserTodoRepository userTodoRepository;

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
        Todo actualTodo = todoService.getTodoById(todoId);

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
        assertThrows(TodoNotFoundException.class, () -> todoService.getTodoById(todoId));
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
        List<Task> actualTasks = todoService.getAllTasksByTodoId(todoId);

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
        List<Task> actualTasks = todoService.getAllTasksByTodoIdOrderedByPriority(todoId);

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
        List<Task> actualCompletedTasks = todoService.getCompletedTasks(todoId);

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
        List<Task> actualCompletedTasks = todoService.getUncompletedTasks(todoId);

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

        when(userService.getUserById(userId)).thenReturn(user);
        when(todoRepository.save(todo)).thenReturn(todo);

        // When
        todoService.addNewTodo(todo, userId);

        // Then
        assertNotNull(todo.getUserTodo());
        verify(userService).getUserById(userId);
        verify(todoRepository, times(2)).save(todo);
    }

    @Test
    void updateTodoTest()
            throws TodoNotFoundException, UserNotFoundException, UserTodoNotFoundException, UserSettingsNotFoundException {
        // Given
        Long todoId = 1L;
        Todo originalTodo = new Todo();
        originalTodo.setUserTodo(UserTodo.builder().user(User.builder()
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
        updatedTodo.setUserTodo(UserTodo.builder().user(User.builder()
                .settings(UserSettings.builder().timeZone("UTC").build()).build()).build());

        when(todoRepository.findById(todoId)).thenReturn(Optional.of(originalTodo));
        when(todoRepository.save(originalTodo)).thenReturn(originalTodo);

        // When
        todoService.updateTodo(updatedTodo, todoId);

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
        todo.setUserTodo(UserTodo.builder().build());

        when(todoRepository.findById(todoId)).thenReturn(Optional.of(todo)).thenReturn(Optional.empty());

        // When
        todoService.deleteTodo(todoId);

        // Then
        verify(todoRepository).findById(todoId);
        verify(userTodoRepository).delete(todo.getUserTodo());
        verify(todoRepository).delete(todo);

        Optional<Todo> deletedTodo = todoRepository.findById(todoId);
        assertFalse(deletedTodo.isPresent());
    }

    @Test
    void addNewUserTodoTest() throws UserNotFoundException, UserTodoNotFoundException {
        // Given
        Todo todo = new Todo();
        todo.setTodoId(1L);

        UserTodo userTodo = new UserTodo();
        userTodo.setUser(User.builder().userId(1L).username("testUser").build());


        // When
        when(userTodoRepository.save(userTodo)).thenReturn(userTodo);
        todoService.addNewUserTodo(todo, userTodo);

        // Then
        assertEquals(todo.getUserTodo(), userTodo);
        verify(userTodoRepository).save(userTodo);
        //verify(mock(TodoService.class)).assignPositionToNewTodo(todo);
    }

    @Test
    void givenNoExistingTodos_whenAssignPositionToNewTodo_thenPositionIsOne() throws UserTodoNotFoundException {
        // Given

        Todo todo = new Todo();
        todo.setTodoId(1L);
        todo.setUserTodo(UserTodo.builder().user(User.builder().build()).build());

        // When
        when(todoRepository.getMaxPosition()).thenReturn(null);

        todoService.assignPositionToNewTodo(todo);

        // Then
        assertEquals(1, todo.getUserTodo().getPosition());
        verify(todoRepository).save(todo);
    }

    @Test
    void givenExistingTodos_whenAssignPositionToNewTodo_thenPositionIsMaxPlusOne() throws UserTodoNotFoundException {
        // Given

        Todo todo = new Todo();
        todo.setTodoId(1L);
        todo.setUserTodo(UserTodo.builder().user(User.builder().build()).build());

        // When
        when(todoRepository.getMaxPosition()).thenReturn(3);


        todoService.assignPositionToNewTodo(todo);

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

        UserTodo userTodo= UserTodo.builder()
                .user(User.builder().userId(1L).username("testUser").build())
                .priorityLevel(5).build();

        todo.setUserTodo(userTodo);
        when(todoRepository.findById(todoId)).thenReturn(Optional.of(todo));

        UserTodo updatedUserTodo = new UserTodo();
        updatedUserTodo.setPriorityLevel(2);

        // When
        todoService.updateUserTodo(todoId, updatedUserTodo);

        // Then
        assertEquals(2, todo.getUserTodo().getPriorityLevel());
    }

    @Test
    public void deleteUserTodoTest() throws UserTodoNotFoundException {
        Long todoId = 1L;
        Todo todo = new Todo();
        todo.setTodoId(todoId);

        UserTodo userTodo= UserTodo.builder().build();
        todo.setUserTodo(userTodo);

        todoService.deleteUserTodo(todo);

        verify(userTodoRepository).delete(todo.getUserTodo());
    }


    @Test
    public void updateTodoPositionTest() throws TodoNotFoundException, UserTodoNotFoundException {

        Long todoId = 1L;
        Todo todo = new Todo();
        todo.setTodoId(todoId);

        UserTodo userTodo= UserTodo.builder().build();
        todo.setUserTodo(userTodo);

        int newPosition = 3;
        Todo todoToUpdate = new Todo();
        todoToUpdate.setTodoId(2L);
        UserTodo userTodoToUpdate = new UserTodo();
        userTodoToUpdate.setPosition(2);
        todoToUpdate.setUserTodo(userTodoToUpdate);
        List<Todo> todosToUpdate = Arrays.asList(todoToUpdate);

        when(todoRepository.findById(todo.getTodoId())).thenReturn(Optional.of(todo));
        when(todoRepository.findTodosWithPositionsBetween(anyInt(), anyInt())).thenReturn(todosToUpdate);

        todoService.updateTodoPosition(todo.getTodoId(), newPosition);

        verify(todoRepository).findTodosWithPositionsBetween(anyInt(), anyInt());
        verify(todoRepository, times(2)).save(any(Todo.class));
    }
}
