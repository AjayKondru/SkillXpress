package com.skillxpress.skillxpress_api.dto;

import java.util.List;
import java.util.Set;

public record CreateTeacherRequest(
        String name,
        String email,
        String phone,
        String qualification,
        Set<Integer> grades,          // 0 = PK, 1-5 or 99 extracurricular
        Set<String> subjects,         // "Maths","Chess"
        List<SlotDto> availableTimes  // [{day:"MONDAY",start:"08:00",end:"16:00"}]
) {}
