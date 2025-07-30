package com.skillxpress.skillxpress_api.dto;

import java.time.LocalDateTime;

public record DashboardClassDto(
        String id,
        String subject,
        int    grade,
        String tutorName,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String joinUrl          // https://meet.jit.si/skillxpress-<id>
) {}
