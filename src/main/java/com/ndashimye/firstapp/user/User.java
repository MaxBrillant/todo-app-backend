package com.ndashimye.firstapp.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ndashimye.firstapp.ZonedDateTimeAttributeConverter;
import com.ndashimye.firstapp.error.AppEntityNotFoundException;
import com.ndashimye.firstapp.userprofile.UserProfile;
import com.ndashimye.firstapp.usersettings.UserSettings;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.mindrot.jbcrypt.BCrypt;
import java.time.ZonedDateTime;
import java.util.Optional;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
    @Id
    @Column(name = "user_id", nullable = false, unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id", nullable = false, unique = true)
    @NotNull(message = "User profile is required")
    private UserProfile profile;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_settings_id", nullable = false, unique = true)
    @NotNull(message = "User settings are required")
    private UserSettings settings;

    @Column(name = "username", nullable = false, unique = true, length = 30)
    @NotNull(message = "Username is required")
    private String username;


    @Column(name = "email_address", nullable = false, unique = true, length = 50)
    @NotNull(message = "User email is required")
    private String emailAddress;


    @Column(name = "password_hash", nullable = false, unique = true, columnDefinition = "BINARY(60)")
    @NotNull(message = "User password is required")
    private String passwordHash;

    @Column(name = "password_salt", unique = true, length = 40, columnDefinition = "BINARY(40)")
    private String passwordSalt;

    @Column(name = "last_login")
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    private ZonedDateTime lastLogin;


    @Column(name = "created_at")
    @CreationTimestamp
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private ZonedDateTime updatedAt;


    public UserProfile getProfile() throws AppEntityNotFoundException {
        return Optional.of(this.profile).orElseThrow(() -> new AppEntityNotFoundException(UserProfile.class));
    }

    public UserSettings getSettings() throws AppEntityNotFoundException {
        return Optional.of(this.settings).orElseThrow(() -> new AppEntityNotFoundException(UserSettings.class));
    }


    public void setPassword(String password) {
        this.passwordSalt = BCrypt.gensalt();
        this.passwordHash = BCrypt.hashpw(password, this.passwordSalt);
    }

}
