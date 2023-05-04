package com.ndashimye.firstapp.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ndashimye.firstapp.ZonedDateTimeAttributeConverter;
import com.ndashimye.firstapp.userprofile.UserProfile;
import com.ndashimye.firstapp.userprofile.UserProfileNotFoundException;
import com.ndashimye.firstapp.usersettings.UserSettings;
import com.ndashimye.firstapp.usersettings.UserSettingsNotFoundException;
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
    @JoinColumn(name = "user_profile_id", unique = true)
    private UserProfile profile;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_settings_id", unique = true)
    private UserSettings settings;

    @Column(name = "username", nullable = false, unique = true, length = 30)
    @NotNull(message = "Username is required")
    private String username;


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


    public UserProfile getProfile() throws UserProfileNotFoundException {
        return Optional.of(this.profile).orElseThrow(() -> new UserProfileNotFoundException());
    }

    public UserSettings getSettings() throws UserSettingsNotFoundException {
        return Optional.of(this.settings).orElseThrow(() -> new UserSettingsNotFoundException());
    }


    public void setPassword(String password) {
        this.passwordSalt = BCrypt.gensalt();
        this.passwordHash = BCrypt.hashpw(password, this.passwordSalt);
    }

    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password, this.passwordHash);
    }

}
