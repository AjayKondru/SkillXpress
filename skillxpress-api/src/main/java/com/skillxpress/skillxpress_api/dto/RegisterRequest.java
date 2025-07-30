package com.skillxpress.skillxpress_api.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

public record RegisterRequest(@Email String email,
                              @Size(min=6) String password,
                              boolean isParent,
                              List<ChildDto> children) {}

