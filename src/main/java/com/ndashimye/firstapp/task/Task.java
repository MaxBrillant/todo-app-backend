package com.ndashimye.firstapp.task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ndashimye.firstapp.ZonedDateTimeAttributeConverter;
import com.ndashimye.firstapp.todo.Todo;
import com.ndashimye.firstapp.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
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
    @JoinColumn(name = "todo_id", nullable = false)
    @NotNull(message = "Todo is required")
    private Todo todo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_task_id")
    private Task parentTask;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "completed_by_user")
    private User CompletedByUser;


    @Column(name = "name", nullable = false, length = 40)
    @NotNull(message = "Name is required")
    private String name;

    @Column(name = "due_time")
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    @FutureOrPresent()
    private ZonedDateTime dueTime;


    @Column(name = "completion_time")
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    @FutureOrPresent()
    private ZonedDateTime completionTime;


    @Column(name = "priority_level")
    @Size(min = 1, max = 5)
    private Integer priorityLevel;


    @Column(name = "position", nullable = false)
    @NotNull(message = "Position is required")
    private int position;


    @OneToMany(mappedBy = "parentTask", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @OrderBy("position")
    private List<Task> childTasks;


    @Column(name = "created_at")
    @CreationTimestamp
    private ZonedDateTime createdAt;


    @Column(name = "updated_at")
    @UpdateTimestamp
    private ZonedDateTime updatedAt;

//
//
//    public ZonedDateTime getDueTime()
//            throws AppEntityNotFoundException {
//
//        ZonedDateTimeAttributeConverter.setDefaultZoneId(ZoneId.of("UTC"));
//        if(dueTime != null) {
//            UserSettings userSettings = this.getUserTask().getTodo().getUserTodo().getUser().getSettings();
//            ZoneId userTimeZone = ZoneId.of(userSettings.getTimeZone());
//            ZonedDateTimeAttributeConverter.setDefaultZoneId(userTimeZone);
//            return ZonedDateTimeAttributeConverter.toDefaultZoneId(dueTime);
//        }
//        return null;
//    }
}
