package com.skillxpress.skillxpress_api.dto;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "teacher_profiles")
@Getter
@Setter
@Builder
public class TeacherProfile {

    @Id
    private String id;                         // same as User.id

    private String name;
    private String qualification;
    private String phone;

    private Set<Integer> eligibleGrades;       // 0 = PK, 1-5, 99 extracurricular
    private Set<String> eligibleSubjects;      // "Maths", "Chess", …

    @Field("availableTimes")                   // embedded sub-document list
    private List<AvailableSlot> availableTimes;

    private boolean enabled = true;            // soft delete

}