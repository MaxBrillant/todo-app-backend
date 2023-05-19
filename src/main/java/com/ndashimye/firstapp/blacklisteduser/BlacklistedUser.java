package com.ndashimye.firstapp.blacklisteduser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ndashimye.firstapp.todo.Todo;
import com.ndashimye.firstapp.userproject.UserProject;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
    @NotBlank(message = "The user project is required")
    private UserProject userProject;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id", nullable = false)
    @NotBlank(message = "The todo is required")
    private Todo todo;
}
