package com.ndashimye.firstapp.task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ndashimye.firstapp.ZonedDateTimeAttributeConverter;
import com.ndashimye.firstapp.project.Project;
import com.ndashimye.firstapp.userproject.UserProject;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Range;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "task")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id", nullable = false, unique = true, updatable = false)
    private Long taskId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @NotNull(message = "The project is required")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_task_id")
    private Task parentTask;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserProject assignedToUser;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "completed_by_user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserProject completedByUser;


    @Column(name = "name", nullable = false)
    @Pattern(regexp = "^[a-zA-Z]([a-zA-Z0-9]|[-_. ](?![._-])){1,48}[a-zA-Z0-9]$"
            , message = "The task name should be 3 to 50 characters long." +
            "It should start with an uppercase or lowercase letter." +
            "It can contain uppercase letters, lowercase letters, digits, spaces, and special characters '-', '_', and '.'." +
            "The special characters '-', '_', and '.' must not appear consecutively or at the beginning or end of the task name.")
    private String name;


    @Column(name = "description")
    @Pattern(regexp = "^[\\w\\s.,;:!?'\\\"(){}\\[\\]-_*&#@^+=|%$\\/]{10,500}$"
            , message = "The task description should be 10 to 500 characters long." +
            "It can contain uppercase letters, lowercase letters, digits, spaces, and common " +
            "punctuation marks (., ,, ;, :, !, ?, ', \", (, ), {, }, [, ], -, _, *, &, #, @, ^, +, =, |, %, $, /).")
    private String description;


    @Column(name = "due_time")
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    @FutureOrPresent(message = "The due time must have a value of a present or future date/time.")
    private ZonedDateTime dueTime;


    @Column(name = "completion_time")
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    @FutureOrPresent(message = "The completion time must have a value of a present or future date/time.")
    private ZonedDateTime completionTime;


    @Column(name = "is_recurrent")
    @Builder.Default
    private Boolean isRecurrent = false;


    @Column(name = "priority_level")
    @Range(min = 0, max = 3, message = "The priority level must be between 0 and 3")
    private Integer priorityLevel;


    @Column(name = "position", nullable = false)
    @NotNull(message = "The position is required")
    private Integer position;


    @OneToMany(mappedBy = "parentTask", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("position")
    private List<Task> childTasks;


    @Column(name = "created_at")
    @CreationTimestamp
    private ZonedDateTime createdAt;


    @Column(name = "updated_at")
    @UpdateTimestamp
    private ZonedDateTime updatedAt;
}
