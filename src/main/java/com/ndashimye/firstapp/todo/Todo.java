package com.ndashimye.firstapp.todo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ndashimye.firstapp.ZonedDateTimeAttributeConverter;
import com.ndashimye.firstapp.project.Project;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotNull(message = "Project is required")
    private Project project;


    @Column(name = "name", nullable = false, length = 40)
    @NotNull(message = "Name is required")
    private String name;


    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "due_time")
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    @FutureOrPresent()
    private ZonedDateTime dueTime;


    @Column(name = "priority_level")
    @Size(min = 1, max = 5)
    private int priorityLevel;


    @Column(name = "position", nullable = false)
    @NotNull(message = "Position is required")
    private int position;


    @Column(name = "is_recurrent")
    private Boolean isRecurrent = false;


    @Column(name = "created_at")
    @CreationTimestamp
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private ZonedDateTime updatedAt;

//    public ZonedDateTime getDueTime() throws AppEntityNotFoundException {
//        ZonedDateTimeAttributeConverter.setDefaultZoneId(ZoneId.of("UTC"));
//        if(dueTime != null) {
//            UserSettings userSettings = this.getUserTodo().getUser().getSettings();
//            ZoneId userTimeZone = ZoneId.of(userSettings.getTimeZone());
//            ZonedDateTimeAttributeConverter.setDefaultZoneId(userTimeZone);
//            return ZonedDateTimeAttributeConverter.toDefaultZoneId(dueTime);
//        }
//        return null;
//    }
}