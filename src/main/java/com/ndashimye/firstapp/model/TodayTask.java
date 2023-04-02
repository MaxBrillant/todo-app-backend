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
@Table(name = "today_task")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TodayTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "today_task_id", nullable = false, updatable = false)
    private Integer todayTaskId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(name = "date")
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    private ZonedDateTime date;


    public ZonedDateTime getDate() {
        ZonedDateTimeAttributeConverter.setDefaultZoneId(ZoneId.of("UTC"));
        if(date != null) {
            if (this.getTask() != null) {
                TodoTask todoTask = this.getTask().getTodoTask();
                if (todoTask != null) {
                    Todo todo = todoTask.getTodo();
                    if (todo != null) {
                        UserTodo userTodo = todo.getUserTodo();
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
                }
            }
            return ZonedDateTimeAttributeConverter.toDefaultZoneId(date);
        }
        return null;
    }

}
