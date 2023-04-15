package com.ndashimye.firstapp.todotask;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ndashimye.firstapp.ZonedDateTimeAttributeConverter;
import com.ndashimye.firstapp.todo.Todo;
import com.ndashimye.firstapp.user.User;
import com.ndashimye.firstapp.usersettings.UserSettings;
import com.ndashimye.firstapp.usertodo.UserTodo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;

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
    @Column(name = "todo_task_id", nullable = false, updatable = false)
    private int todoTaskId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_id", nullable = false)
    @NotBlank(message = "todo is required")
    private Todo todo;

    @Column(name = "completion_time")
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    private ZonedDateTime completionTime;

    @Column(name = "order", nullable = false)
    @NotBlank(message = "order is required")
    private int order;

    @Column(name = "priority_level")
    private Integer priorityLevel;

    @Column(name = "is_completed")
    private Boolean isCompleted;



    public ZonedDateTime getCompletionTime() {
        ZonedDateTimeAttributeConverter.setDefaultZoneId(ZoneId.of("UTC"));
        if(completionTime != null) {
            if (this.getTodo() != null) {
                UserTodo userTodo = this.getTodo().getUserTodo();
                if (userTodo != null) {
                    User user = userTodo.getUser();
                    if (user != null) {
                        UserSettings userSettings = user.getSettings();
                        if (userSettings != null) {
                            ZoneId userTimeZone = ZoneId.of(userSettings.getTimeZone());
                            ZonedDateTimeAttributeConverter.setDefaultZoneId(userTimeZone);
                        }
                    }
                }
            }
            return ZonedDateTimeAttributeConverter.toDefaultZoneId(completionTime);
        }
        return null;
    }

}
