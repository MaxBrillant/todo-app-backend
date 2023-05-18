package com.ndashimye.firstapp.blacklisteduser;

import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.todo.Todo;
import com.ndashimye.firstapp.todo.TodoRepository;
import com.ndashimye.firstapp.todo.TodoService;
import com.ndashimye.firstapp.userproject.UserProject;
import com.ndashimye.firstapp.userproject.UserProjectService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class BlackListedUserServiceImpl implements BlackListedUserService {

    private final UserProjectService userProjectService;
    private final TodoService todoService;
    private TodoRepository todoRepository;
    private BlacklistedUserRepository blacklistedUserRepository;



    /*

    Service methods that handle all the operations
    related to the relationship between users and their restricted todos

    */

    @Override
    public List<Todo> getRestrictedTodosOfUserInProject(Long projectId, Long userId)
            throws AppEntityNotFoundException {

        UserProject userProject = userProjectService
                .getUserProjectByUserIdAndProjectId(userId, projectId);

        log.info("Fetching all blacklisted todos of user of ID: {} and username: {} in project of ID: {}..."
                , userProject.getUser().getUserId(), userProject.getUser().getUsername()
                , userProject.getProject().getProjectId());

        List<Todo> blackListedTodos = todoRepository
                .findBlacklistedTodosOfUserAndOrderByPositionAsc(userProject);

        log.info("All blacklisted todos of user of ID: {} and username: {} in project of ID: {}" +
                        " were successfully fetched."
                , userProject.getUser().getUserId(), userProject.getUser().getUsername()
                , userProject.getProject().getProjectId());

        return blackListedTodos;
    }

    @Override
    public void restrictUserFromAccessingTodoInProject(Long userId, Long projectId, Long todoId)
            throws AppEntityNotFoundException {

        UserProject userProject = userProjectService
                .getUserProjectByUserIdAndProjectId(userId, projectId);

        Todo todo = todoService.getTodoById(todoId);
        log.info("Restricting user of ID: {} and username: {} from accessing todo of ID: {}..."
                , userProject.getUser().getUserId(), userProject.getUser().getUsername(), todo.getTodoId());

        blacklistedUserRepository.save(BlacklistedUser.builder()
                .userProject(userProject)
                .todo(todo)
                .build());

        log.info("User of ID: {} and username: {} was successfully restricted from accessing todo of ID: {}."
                , userProject.getUser().getUserId(), userProject.getUser().getUsername(), todo.getTodoId());
    }
}
