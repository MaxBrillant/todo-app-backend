package com.ndashimye.firstapp.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ndashimye.firstapp.ZonedDateTimeAttributeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Data
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
    private Todo todo;

    @Column(name = "completion_time")
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    private ZonedDateTime completionTime;

    @Column(name = "todo_task_order", nullable = false)
    private int order;

    @Column(name = "priority_level")
    private Integer priorityLevel;

    @Column(name = "completed")
    private Boolean completed;



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
