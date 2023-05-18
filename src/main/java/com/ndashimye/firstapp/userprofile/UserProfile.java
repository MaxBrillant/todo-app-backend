package com.ndashimye.firstapp.userprofile;

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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_profile")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_profile_id", nullable = false, unique = true, updatable = false)
    private Long userProfileId;

    @Column(name = "first_name", nullable = false, length = 40)
    @NotNull(message = "First name is required")
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 40)
    @NotNull(message = "Last name is required")
    private String lastName;

    @Column(name = "profile_image_url", length = 255)
    private String profileImageUrl;


    @Column(name = "created_at")
    @CreationTimestamp
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private ZonedDateTime updatedAt;
}
