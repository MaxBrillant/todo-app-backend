package com.ndashimye.firstapp.userprofile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
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

    @Column(name = "first_name")
    @Pattern(regexp = "^(?=\\P{M}\\p{L}{2,70}$)[\\p{L}\\p{M}]+$"
            , message = "The first name must have a length that is between 2 and 70 characters, excluding combining marks.")
    private String firstName;

    @Column(name = "last_name")
    @Pattern(regexp = "^(?=\\P{M}\\p{L}{2,70}$)[\\p{L}\\p{M}]+$"
            , message = "The last name must have a length that is between 2 and 70 characters, excluding combining marks.")
    private String lastName;

    @Column(name = "profile_image_url")
    @Pattern(regexp = "^https?:\\/\\/.+(\\.(?i)(jpg|jpeg|png|webp|svg))$"
            , message = "The link should start with http:// or https://." +
            "The link should contain a valid domain name and path." +
            "The image file extension should be one of the following: jpg, jpeg, png, webp, svg.")
    private String profileImageUrl;


    @Column(name = "created_at")
    @CreationTimestamp
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private ZonedDateTime updatedAt;
}
