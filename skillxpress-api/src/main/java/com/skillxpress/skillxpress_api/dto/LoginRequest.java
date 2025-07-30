package com.skillxpress.skillxpress_api.dto;

import jakarta.validation.constraints.Email;

public record LoginRequest(@Email String email, String password) {}
