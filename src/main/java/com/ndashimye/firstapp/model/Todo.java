package com.ndashimye.firstapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ndashimye.firstapp.ZonedDateTimeAttributeConverter;
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
@Table(name = "todo")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id", nullable = false, updatable = false)
    private Integer todoId;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_todo_id", unique = true)
    private UserTodo userTodo;

    @Column(name = "name", length = 40, nullable = false)
    @NotBlank(message = "name is required")
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "due_time")
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    private ZonedDateTime dueTime;


    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;


    public ZonedDateTime getDueTime() {
        ZonedDateTimeAttributeConverter.setDefaultZoneId(ZoneId.of("UTC"));
        if(dueTime != null) {
            if (this.getUserTodo() != null) {
                User user = this.getUserTodo().getUser();
                if (user != null) {
                    UserSettings userSettings = user.getSettings();
                    if (userSettings != null) {
                        ZoneId userTimeZone = ZoneId.of(userSettings.getTimeZone());
                        ZonedDateTimeAttributeConverter.setDefaultZoneId(userTimeZone);
                    }
                }
            }
            return ZonedDateTimeAttributeConverter.toDefaultZoneId(dueTime);
        }
        return null;
    }
}