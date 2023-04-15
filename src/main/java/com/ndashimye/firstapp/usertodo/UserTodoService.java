package com.ndashimye.firstapp.usertodo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserTodoService {

    @Autowired
    UserTodoRepository userTodoRepository;

    public UserTodo getUserTodoById(Integer userTodoId) throws UserTodoNotFoundException {

        Optional<UserTodo> userTodo = userTodoRepository.findById(userTodoId);

        if(!userTodo.isPresent()){
            throw new UserTodoNotFoundException();
        }
        return userTodo.get();

    }

    public void addNewUserTodo(UserTodo userTodo) {
        //set userTodo order
        userTodoRepository.save(userTodo);
    }

    public void updateUserTodo(UserTodo updatedUserTodo, UserTodo userTodo) {

        if (Objects.nonNull(updatedUserTodo.getUser()) && !updatedUserTodo.getUser().equals("")) {
            userTodo.setUser(updatedUserTodo.getUser());
        }
        if (Objects.nonNull(updatedUserTodo.getOrder()) && !String.valueOf(updatedUserTodo.getOrder()).equals("")) {
            userTodo.setOrder(updatedUserTodo.getOrder());
        }

        if (Objects.nonNull(updatedUserTodo.getPriorityLevel()) && !String.valueOf(updatedUserTodo.getPriorityLevel()).equals("")) {
            userTodo.setPriorityLevel(updatedUserTodo.getPriorityLevel());
        }

        userTodoRepository.save(userTodo);
    }

    public void deleteUserTodo(UserTodo userTodo) {
        userTodoRepository.delete(userTodo);
    }
}
