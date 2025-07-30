package com.skillxpress.skillxpress_api.dto;

import java.util.List;

public record AuthResponse(String token, List<String> childIds) {}
