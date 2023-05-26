package com.ndashimye.firstapp.blacklisteduser;

import com.ndashimye.firstapp.todo.Todo;
import com.ndashimye.firstapp.userproject.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlacklistedUserRepository extends JpaRepository<BlacklistedUser, Long> {
    Optional<BlacklistedUser> findByUserProjectAndTodo(UserProject userProject, Todo todo);
}
