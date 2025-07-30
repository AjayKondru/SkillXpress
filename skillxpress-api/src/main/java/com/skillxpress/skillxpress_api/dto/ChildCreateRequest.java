package com.skillxpress.skillxpress_api.dto;

import jakarta.validation.constraints.*;

public record ChildCreateRequest(
        @NotBlank String name,
        @Min(0) @Max(12) int grade,
        @NotBlank String timeZone          // e.g. "Asia/Kolkata"
) {}
