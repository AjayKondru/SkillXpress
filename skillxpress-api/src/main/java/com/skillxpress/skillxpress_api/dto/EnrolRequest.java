package com.skillxpress.skillxpress_api.dto;


import jakarta.validation.constraints.NotBlank;

/** Parent (or student) calls /classes/{id}/enrol */
public record EnrolRequest(
        @NotBlank String childId,          // which child is being enrolled
        @NotBlank String paymentGateway    // "RAZORPAY" | "STRIPE"
) {}
