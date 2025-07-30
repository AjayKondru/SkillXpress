package com.skillxpress.skillxpress_api.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Document("users")
public class User {
    @Id
    private String id;
    private String email;
    private String passwordHash;     // BCrypt
    private Role role;
    private ParentProfile parentProfile;
    private TutorProfile tutorProfile;
    private boolean enabled = true;
    private boolean firstLogin = true;
    public enum Role {PARENT, STUDENT, TUTOR, ADMIN}
}


