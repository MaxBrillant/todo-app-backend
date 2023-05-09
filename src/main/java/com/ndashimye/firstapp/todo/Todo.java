package com.ndashimye.firstapp.todo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ndashimye.firstapp.ZonedDateTimeAttributeConverter;
import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.todoproject.TodoProject;
import com.ndashimye.firstapp.usersettings.UserSettings;
import com.ndashimye.firstapp.usertodo.UserTodo;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

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


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_todo_id", nullable = false, unique = true)
    @NotNull(message = "User todo is required")
    private UserTodo userTodo;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_todo_id", nullable = false, unique = true)
    @NotNull(message = "Project todo is required")
    private TodoProject todoProject;


    @Column(name = "name", nullable = false, length = 40)
    @NotNull(message = "Name is required")
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "due_time")
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    @FutureOrPresent()
    private ZonedDateTime dueTime;

    @Column(name = "is_recurrent")
    private Boolean isRecurrent;


    @Column(name = "created_at")
    @CreationTimestamp
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private ZonedDateTime updatedAt;


    public UserTodo getUserTodo() throws AppEntityNotFoundException {
        return Optional.of(this.userTodo).orElseThrow(() -> new AppEntityNotFoundException(UserTodo.class));
    }


    public ZonedDateTime getDueTime() throws AppEntityNotFoundException {
        ZonedDateTimeAttributeConverter.setDefaultZoneId(ZoneId.of("UTC"));
        if(dueTime != null) {
            UserSettings userSettings = this.getUserTodo().getUser().getSettings();
            ZoneId userTimeZone = ZoneId.of(userSettings.getTimeZone());
            ZonedDateTimeAttributeConverter.setDefaultZoneId(userTimeZone);
            return ZonedDateTimeAttributeConverter.toDefaultZoneId(dueTime);
        }
        return null;
    }
}