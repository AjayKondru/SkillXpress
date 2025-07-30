package com.skillxpress.skillxpress_api.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Document("subscriptions")
public class Subscription {
    @Id
    private String id;
    private String childId;
    private String classId;
    private LocalDateTime startDate;
    private LocalDateTime nextBillingDate;
    private double amount;
    private Status status;
    public enum Status {ACTIVE, CANCELLED, PAST_DUE}
}
