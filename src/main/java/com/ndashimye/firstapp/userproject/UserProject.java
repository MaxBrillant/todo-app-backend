package com.ndashimye.firstapp.userproject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ndashimye.firstapp.project.Project;
import com.ndashimye.firstapp.user.User;
import jakarta.persistence.*;
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
@Table(name = "user_project")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_project_id", nullable = false, unique = true, updatable = false)
    private Long userProjectId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "The user is required")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @NotNull(message = "The project is required")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Project project;


    @Column(name = "role", nullable = false)
    @NotNull(message = "The project role is required")
    @Enumerated(EnumType.STRING)
    private ProjectRole projectRole;


    @Column(name = "position", nullable = false)
    @NotNull(message = "The position is required")
    private Integer position;
}
