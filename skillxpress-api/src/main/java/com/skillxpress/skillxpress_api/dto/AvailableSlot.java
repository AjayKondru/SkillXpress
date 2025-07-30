package com.skillxpress.skillxpress_api.dto;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import jakarta.persistence.Embeddable;


@Embeddable
public record AvailableSlot(
        DayOfWeek day,
        LocalTime start,
        LocalTime end
) implements Serializable {}
