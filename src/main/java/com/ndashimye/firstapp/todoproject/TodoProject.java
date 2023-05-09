package com.ndashimye.firstapp.todoproject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ndashimye.firstapp.project.Project;
import com.ndashimye.firstapp.project.ProjectNotFoundException;
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
@Table(name = "todo_project")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TodoProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_project_id", nullable = false, unique = true, updatable = false)
    private Long userTodoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private Project project;

    @Column(name = "position", nullable = false)
    @NotNull(message = "Position of todo is required")
    private int position;


    public Project getProject() throws ProjectNotFoundException {
        return Optional.of(this.project).orElseThrow(() -> new ProjectNotFoundException());
    }
}
