package com.skillxpress.skillxpress_api.model;

import lombok.*;

import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TutorProfile {
    private String fullName;
    private List<String> subjects; // e.g. "Math", "Chess"
    private List<Integer> grades; // teachable grades
    private String bio;
    private double ratingAverage;
    private int ratingCount;
}
