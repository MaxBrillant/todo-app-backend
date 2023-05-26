package com.ndashimye.firstapp.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ndashimye.firstapp.ZonedDateTimeAttributeConverter;
import com.ndashimye.firstapp.userprofile.UserProfile;
import com.ndashimye.firstapp.usersettings.UserSettings;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.mindrot.jbcrypt.BCrypt;
import java.time.ZonedDateTime;

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
    @NotNull(message = "The user profile is required")
    private UserProfile profile;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_settings_id", nullable = false, unique = true)
    @NotNull(message = "The user settings are required")
    private UserSettings settings;

    @Column(name = "username", nullable = false, unique = true)
    @Pattern(regexp = "^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$"
            , message = "The username should be 8 to 20 characters long. " +
            "It should start with an alphanumeric character and can contain lowercase letters, digits, and special characters '_' and '.'." +
            "The special characters '_' and '.' should not appear consecutively or at the beginning or end of the username")
    private String username;


    @Column(name = "email_address", nullable = false, unique = true)
    @NotBlank(message = "The user email is required.")
    @Size(min = 10, message = "The email address must at least 10 characters long")
    @Email(message = "The email address must be valid and have the appropriate format")
    private String emailAddress;


    @Column(name = "password_hash", nullable = false, unique = true, columnDefinition = "BINARY(60)")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()_[-{}]:;',?/*~$^+=<>]).{8,20}$"
            , message = "The password should be 8 to 20 characters long. " +
            "It should contain at least one lowercase letter, one uppercase letter, one digit, " +
            "and one special character")
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
}
