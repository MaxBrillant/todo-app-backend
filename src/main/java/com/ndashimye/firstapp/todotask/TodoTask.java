package com.ndashimye.firstapp.todotask;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ndashimye.firstapp.ZonedDateTimeAttributeConverter;
import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.todo.Todo;
import com.ndashimye.firstapp.usersettings.UserSettings;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "todo_task")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TodoTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_task_id", nullable = false, unique = true, updatable = false)
    private Long todoTaskId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id", nullable = false)
    @NotNull(message = "Todo is required")
    private Todo todo;

    @Column(name = "completion_time")
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    @FutureOrPresent()
    private ZonedDateTime completionTime;

    @Column(name = "position", nullable = false)
    @NotNull(message = "Position of task is required")
    private int position;

    @Column(name = "priority_level")
    @Size(min = 1, max = 5)
    private Integer priorityLevel;

    @Column(name = "is_completed")
    private Boolean isCompleted;


    public Todo getTodo() throws AppEntityNotFoundException {
        return Optional.of(this.todo).orElseThrow(() -> new AppEntityNotFoundException(Todo.class));
    }

    public ZonedDateTime getCompletionTime()
            throws AppEntityNotFoundException {

        ZonedDateTimeAttributeConverter.setDefaultZoneId(ZoneId.of("UTC"));
        if(completionTime != null) {
            UserSettings userSettings = this.getTodo().getUserTodo().getUser().getSettings();
            ZoneId userTimeZone = ZoneId.of(userSettings.getTimeZone());
            ZonedDateTimeAttributeConverter.setDefaultZoneId(userTimeZone);
            }
            return ZonedDateTimeAttributeConverter.toDefaultZoneId(completionTime);
    }

}
