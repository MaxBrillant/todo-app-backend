package com.ndashimye.firstapp.blacklisteduser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ndashimye.firstapp.todo.Todo;
import com.ndashimye.firstapp.userproject.UserProject;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "blacklisted_user")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class BlacklistedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "blacklisted_user_id", nullable = false, unique = true, updatable = false)
    private Long blacklistedUserId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_project_id", nullable = false)
    @NotNull(message = "The user project is required")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserProject userProject;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id", nullable = false)
    @NotNull(message = "The todo is required")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Todo todo;
}
