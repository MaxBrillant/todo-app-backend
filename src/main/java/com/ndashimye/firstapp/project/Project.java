package com.ndashimye.firstapp.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.parameters.P;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "project")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id", nullable = false, unique = true, updatable = false)
    private Long projectId;


    @Column(name = "name", nullable = false)
    @Pattern(regexp = "^[a-zA-Z]([a-zA-Z0-9]|[-_.](?![._-])){1,48}[a-zA-Z0-9]$"
            , message = "The project name should be 3 to 50 characters long." +
            "It should start with an uppercase or lowercase letter." +
            "It can contain uppercase letters, lowercase letters, digits, spaces, and special characters '-', '_', and '.'." +
            "The special characters '-', '_', and '.' must not appear consecutively or at the beginning or end of the project name.")
    private String name;


    @Column(name = "description")
    @Pattern(regexp = "^[\\w\\s.,;:!?'\\\"(){}\\[\\]-_*&#@^+=|%$\\/]{10,500}$"
            , message = "The project description should be 10 to 500 characters long." +
            "It can contain uppercase letters, lowercase letters, digits, spaces, and common " +
            "punctuation marks (., ,, ;, :, !, ?, ', \", (, ), {, }, [, ], -, _, *, &, #, @, ^, +, =, |, %, $, /).")
    private String description;


    @Column(name = "cover_image_url")
    @Pattern(regexp = "^https?:\\/\\/.+(\\.(?i)(jpg|jpeg|png|webp|svg))$"
            , message = "The link should start with http:// or https://." +
            "The link should contain a valid domain name and path." +
            "The image file extension should be one of the following: jpg, jpeg, png, webp, svg.")
    private String coverImageUrl;


    @Column(name = "created_at")
    @CreationTimestamp
    private ZonedDateTime createdAt;


    @Column(name = "updated_at")
    @UpdateTimestamp
    private ZonedDateTime updatedAt;
}
