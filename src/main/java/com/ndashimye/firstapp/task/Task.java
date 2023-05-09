package com.ndashimye.firstapp.task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ndashimye.firstapp.ZonedDateTimeAttributeConverter;
import com.ndashimye.firstapp.todo.TodoNotFoundException;
import com.ndashimye.firstapp.todotask.TodoTask;
import com.ndashimye.firstapp.todotask.TodoTaskNotFoundException;
import com.ndashimye.firstapp.user.UserNotFoundException;
import com.ndashimye.firstapp.usersettings.UserSettings;
import com.ndashimye.firstapp.usersettings.UserSettingsNotFoundException;
import com.ndashimye.firstapp.usertodo.UserTodoNotFoundException;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_task_id", nullable = false, unique = true)
    @NotNull(message = "Todo task is required")
    private TodoTask todoTask;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_task_id")
    private Task parentTask;

    @Column(name = "name", nullable = false, length = 40)
    @NotNull(message = "Name is required")
    private String name;

    @Column(name = "due_time")
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    @FutureOrPresent()
    private ZonedDateTime dueTime;

    @OneToMany(mappedBy = "parentTask", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Task> childTasks;

    @Column(name = "created_at")
    @CreationTimestamp
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private ZonedDateTime updatedAt;



    public ZonedDateTime getDueTime()
            throws TodoTaskNotFoundException, TodoNotFoundException
            , UserTodoNotFoundException, UserNotFoundException, UserSettingsNotFoundException {

        ZonedDateTimeAttributeConverter.setDefaultZoneId(ZoneId.of("UTC"));
        if(dueTime != null) {
            UserSettings userSettings = this.getTodoTask().getTodo().getUserTodo().getUser().getSettings();
            ZoneId userTimeZone = ZoneId.of(userSettings.getTimeZone());
            ZonedDateTimeAttributeConverter.setDefaultZoneId(userTimeZone);
            return ZonedDateTimeAttributeConverter.toDefaultZoneId(dueTime);
        }
        return null;
    }


    public TodoTask getTodoTask() throws TodoTaskNotFoundException {
        return Optional.of(this.todoTask).orElseThrow(() -> new TodoTaskNotFoundException());
    }
}
