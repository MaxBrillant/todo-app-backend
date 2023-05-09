package com.ndashimye.firstapp.usertodo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotNull(message = "User is required")
    private User user;

    @Column(name = "position", nullable = false)
    @NotNull(message = "Position of todo is required")
    private int position;

    @Column(name = "priority_level")
    @Size(min = 1, max = 5)
    private int priorityLevel;


    public User getUser() throws AppEntityNotFoundException {
        return Optional.of(this.user).orElseThrow(() -> new AppEntityNotFoundException(User.class));
    }
}
