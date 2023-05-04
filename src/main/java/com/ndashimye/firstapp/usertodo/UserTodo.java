package com.ndashimye.firstapp.usertodo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ndashimye.firstapp.user.User;
import com.ndashimye.firstapp.user.UserNotFoundException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Optional;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_todo")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserTodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_todo_id", nullable = false, unique = true, updatable = false)
    private Long userTodoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "user is required")
    private User user;

    @Column(name = "position", nullable = false)
    @NotNull(message = "position of todo of is required")
    private int position;

    @Column(name = "priority_level")
    private int priorityLevel;


    public User getUser() throws UserNotFoundException {
        return Optional.of(this.user).orElseThrow(() -> new UserNotFoundException());
    }
}
