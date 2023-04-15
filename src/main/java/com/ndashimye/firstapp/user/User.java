package com.ndashimye.firstapp.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ndashimye.firstapp.ZonedDateTimeAttributeConverter;
import com.ndashimye.firstapp.userprofile.UserProfile;
import com.ndashimye.firstapp.usersettings.UserSettings;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
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
    @Column(name = "user_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id", unique = true)
    private UserProfile profile;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_settings_id", unique = true)
    private UserSettings settings;

    @Column(name = "username", nullable = false, unique = true, length = 30)
    @NotBlank(message = "Username is required")
    private String username;


    @Column(name = "password_hash", nullable = false, unique = true, columnDefinition = "BINARY(60)")
    @NotBlank(message = "User password is required")
    private String passwordHash;

    @Column(name = "password_salt", unique = true, length = 40, columnDefinition = "BINARY(40)")
    private String passwordSalt;

    @Column(name = "last_login")
    @Convert(converter = ZonedDateTimeAttributeConverter.class)
    private ZonedDateTime lastLogin;


    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;



    public void setPassword(String password) {
        this.passwordSalt = BCrypt.gensalt();
        this.passwordHash = BCrypt.hashpw(password, this.passwordSalt);
    }

    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password, this.passwordHash);
    }


//    @PostLoad
//    private void convertLastLoginToUserTimeZone() {
//        if (lastLogin != null && userSettings != null) {
//            String timeZoneId = userSettings.getTimeZone();
//            ZoneId userTimeZone = ZoneId.of(timeZoneId);
//            lastLogin = lastLogin.withZoneSameInstant(userTimeZone);
//        }
//    }

}
