package com.ndashimye.firstapp.usersettings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    @NotBlank(message = "The time zone is required")
    @Pattern(regexp = "^UTC[+-](1[0-4]|0\\d)(:00)?$"
            , message = "timezone offsets in the format of 'UTC±N' or 'UTC±N:00'" +
            ", where N represents the offset in hours from -14 to +14.")
    private String timeZone;


    @Column(name = "theme", nullable = false)
    @NotNull(message = "The theme is required")
    @Enumerated(EnumType.STRING)
    private Theme theme;


    @Column(name = "language", nullable = false)
    @NotNull(message = "The language is required")
    @Enumerated(EnumType.STRING)
    private Language language;


    @Column(name = "created_at")
    @CreationTimestamp
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private ZonedDateTime updatedAt;
}
