package com.skillxpress.skillxpress_api.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Document("ratings")
public class Rating {
    @Id
    private String id;
    private String classId;
    private String childId;
    private int stars; // 1-5
    private String comment;
    private LocalDateTime createdAt;
}
