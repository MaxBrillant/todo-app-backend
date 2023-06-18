package com.ndashimye.firstapp.user;

import com.ndashimye.firstapp.blacklisteduser.BlackListedUserService;
import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.error.InvalidTimeFormatException;
import com.ndashimye.firstapp.project.Project;
import com.ndashimye.firstapp.project.ProjectService;
import com.ndashimye.firstapp.task.TaskService;
import com.ndashimye.firstapp.todo.Todo;
import com.ndashimye.firstapp.todo.TodoDTO;
import com.ndashimye.firstapp.todo.TodoService;
import com.ndashimye.firstapp.userproject.UserProject;
import com.ndashimye.firstapp.userproject.UserProjectDTO;
import com.ndashimye.firstapp.userproject.UserProjectService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final TodoService todoService;
    private final TaskService taskService;
    private final UserProjectService userProjectService;
    private final BlackListedUserService blackListedUserService;



    //HTTP endpoints that handle all the operations related to users

    @PostMapping()
    public void addUser(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        userService.addNewUser(userRegistrationDTO);
    }

    @PutMapping("/{userId}")
    public void updateUser(@RequestBody UserRegistrationDTO updatedUser,
                           @PathVariable Long userId)
            throws AppEntityNotFoundException {

        userService.updateUser(updatedUser, userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId)
            throws AppEntityNotFoundException {
        userService.deleteUser(userId);
    }


    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDTO getUserById(@PathVariable Long userId)
            throws AppEntityNotFoundException {

        return userService.getUserDTOById(userId);
    }

    @GetMapping("/email/{emailAddress}")
    public UserDTO getUserByEmailAddress(@PathVariable String emailAddress)
            throws AppEntityNotFoundException {

        return userService.getUserByEmailAddress(emailAddress);
    }

    @GetMapping("/username/{username}")
    public UserDTO getUserByUsername(@PathVariable String username)
            throws AppEntityNotFoundException {

        return userService.getUserByUsername(username);
    }



    /*

    HTTP endpoints that handle all the operations
    related to the relationship between users and projects

    */

    @GetMapping("/{userId}/projects/{projectId}/todos")
    public List<TodoDTO> getTodosOfUserByProjectId(@PathVariable Long userId,
                                                   @PathVariable Long projectId)
            throws AppEntityNotFoundException {

        return todoService.getAllTodosOfUserByProjectId(userId, projectId);
    }

    @GetMapping("/{userId}/projects")
    public List<UserProjectDTO> getProjectsByUserId(@PathVariable Long userId)
            throws AppEntityNotFoundException {

        return userProjectService.getAllUserProjects(userId);
    }



    /*

    HTTP endpoints that handle all the operations
    related to the relationship between users and todos

    */

    @GetMapping("/{userId}/todos")
    public List<TodoDTO> getTodosByUserId(@PathVariable Long userId)
            throws AppEntityNotFoundException {

        return todoService.getAllTodosByUserId(userId);
    }

    @GetMapping("/{userId}/todos/order-by/due-time/most-recent")
    public List<TodoDTO> getTodosByUserIdOrderedByMostRecent(@PathVariable Long userId)
            throws AppEntityNotFoundException {

        return todoService.getAllTodosByUserIdOrderedByMostRecent(userId);
    }

    @GetMapping("/{userId}/todos/order-by/due-time/least-recent")
    public List<TodoDTO> getTodosByUserIdOrderedByLeastRecent(@PathVariable Long userId)
            throws AppEntityNotFoundException {

        return todoService.getAllTodosByUserIdOrderedByLeastRecent(userId);
    }

    @GetMapping("/{userId}/todos/between")
    public List<TodoDTO> getTodosBetweenDates
            (@PathVariable Long userId
                    , @RequestParam String start, @RequestParam String end)
            throws AppEntityNotFoundException, InvalidTimeFormatException {

            return todoService.getAllTodosByUserIdBetweenDates(userId, start, end);

    }



    /*

    HTTP endpoints that handle all the operations
    related to the relationship between users and restricted todos

    */

    @GetMapping("/{userId}/projects/{projectId}/restricted-todos")
    public List<TodoDTO> getRestrictedTodosOfUserInProject
            (@PathVariable Long userId, @PathVariable Long projectId)
            throws AppEntityNotFoundException {

        return blackListedUserService.getRestrictedTodosOfUserInProject(userId, projectId);
    }


    @PutMapping("/{userId}/projects/{projectId}/todos/{todoId}/restrict-access")
    public void restrictUserFromAccessingTodoInProject
            (@PathVariable Long userId, @PathVariable Long projectId
                    , @PathVariable Long todoId)
            throws AppEntityNotFoundException {

        blackListedUserService.restrictUserFromAccessingTodoInProject(userId, projectId, todoId);
    }

    @PutMapping("/{userId}/projects/{projectId}/todos/{todoId}/unrestrict")
    public void unrestrictUserFromAccessingTodoInProject
            (@PathVariable Long userId, @PathVariable Long projectId
                    , @PathVariable Long todoId)
            throws AppEntityNotFoundException {

        blackListedUserService.unrestrictUserFromAccessingTodoInProject(userId, projectId, todoId);
    }


    @PutMapping("/{userId}/tasks/{taskId}/complete")
    public void completeTask(@PathVariable Long taskId, @PathVariable Long userId)
            throws AppEntityNotFoundException {

        taskService.completeTask(taskId, userId);
    }
}
