package com.ndashimye.firstapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_todo")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserTodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_todo_id", nullable = false, updatable = false)
    private Integer userTodoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "user_todo_order", nullable = false)
    private int order;

    @Column(name = "priority_level")
    private int priorityLevel;

}
