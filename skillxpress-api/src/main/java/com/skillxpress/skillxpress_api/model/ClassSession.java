package com.skillxpress.skillxpress_api.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Document("classes")
public class ClassSession {
    @Id
    private String id;
    private String subject;
    private int grade;
    private String tutorId;
    private List<String> studentIds;
    private int capacity; // max 4
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean recorded;
}
