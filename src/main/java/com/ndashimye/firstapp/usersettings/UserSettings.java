package com.ndashimye.firstapp.usersettings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_settings")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_settings_id", nullable = false, unique = true, updatable = false)
    private Long userSettingsId;

    @Column(name = "time_zone", nullable = false, length = 60)
    @NotNull(message = "time zone is required")
    private String timeZone;


    @Column(name = "theme", nullable = false)
    @NotNull(message = "Theme is required")
    @Enumerated(EnumType.STRING)
    private Theme theme;


    @Column(name = "language", nullable = false)
    @NotNull(message = "Language is required")
    @Enumerated(EnumType.STRING)
    private Language language;


    @Column(name = "created_at")
    @CreationTimestamp
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private ZonedDateTime updatedAt;
}
