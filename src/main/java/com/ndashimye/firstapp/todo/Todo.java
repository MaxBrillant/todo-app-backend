package com.ndashimye.firstapp.todo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ndashimye.firstapp.ZonedDateTimeAttributeConverter;
import com.ndashimye.firstapp.project.Project;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "todo")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id", nullable = false, unique = true, updatable = false)
    private Long todoId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @NotBlank(message = "The project is required")
    private Project project;


    @Column(name = "name", nullable = false)
    @Pattern(regexp = "^[a-zA-Z]([a-zA-Z0-9]|[-_.](?![._-])){1,48}[a-zA-Z0-9]$"
            , message = "The todo name should be 3 to 50 characters long." +
            "It should start with an uppercase or lowercase letter." +
            "It can contain uppercase letters, lowercase letters, digits, spaces, and special characters '-', '_', and '.'." +
            "The special characters '-', '_', and '.' must not appear consecutively or at the beginning or end of the todo name.")
    private String name;


    @Column(name = "description")
    @Pattern(regexp = "^[\\w\\s.,;:!?'\\\"(){}\\[\\]-_*&#@^+=|%$\\/]{10,500}$"
            , message = "The project description should be 10 to 500 characters long." +
            "It can contain uppercase letters, lowercase letters, digits, spaces, and common " +
            "punctuation marks (., ,, ;, :, !, ?, ', \", (, ), {, }, [, ], -, _, *, &, #, @, ^, +, =, |, %, $, /).")
    private String description;

    @Column(name = "due_time")
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    @FutureOrPresent(message = "The due time must have a value of a present or future date/time.")
    private ZonedDateTime dueTime;


    @Column(name = "priority_level")
    @Size(min = 1, max = 5, message = "The priority level must be between 1 and 5")
    private int priorityLevel;


    @Column(name = "position", nullable = false)
    @NotBlank(message = "The position is required")
    private int position;


    @Column(name = "is_recurrent")
    private Boolean isRecurrent = false;


    @Column(name = "created_at")
    @CreationTimestamp
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private ZonedDateTime updatedAt;
}