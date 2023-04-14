package com.ndashimye.firstapp.usertodo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ndashimye.firstapp.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_todo")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserTodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_todo_id", nullable = false, updatable = false)
    private Integer userTodoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotBlank(message = "user is required")
    private User user;

    @Column(name = "order", nullable = false)
    @NotBlank(message = "order is required")
    private int order;

    @Column(name = "priority_level")
    private int priorityLevel;

}
