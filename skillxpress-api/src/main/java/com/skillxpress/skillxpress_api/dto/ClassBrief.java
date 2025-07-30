package com.skillxpress.skillxpress_api.dto;

import java.time.LocalDateTime;

public record ClassBrief(String id, String tutorName, String subject, int grade, LocalDateTime startTime, int capacity) {}
