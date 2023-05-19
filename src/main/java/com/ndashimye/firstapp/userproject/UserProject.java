package com.ndashimye.firstapp.userproject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ndashimye.firstapp.project.Project;
import com.ndashimye.firstapp.user.User;
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
@Table(name = "user_project")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_project_id", nullable = false, unique = true, updatable = false)
    private Long userTodoId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotBlank(message = "The user is required")
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @NotBlank(message = "The project is required")
    private Project project;


    @Column(name = "role", nullable = false)
    @NotBlank(message = "The project role is required")
    @Enumerated(EnumType.STRING)
    private ProjectRole projectRole;


    @Column(name = "position", nullable = false)
    @NotBlank(message = "The position is required")
    private int position;
}
