package com.health.app.adapter.in.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(
        @NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format")
                String email,
        @NotBlank(message = "Password cannot be blank")
                @Size(min = 8, message = "Password must be at least 8 characters")
                String password,
        @NotBlank(message = "Name cannot be blank")
                @Size(max = 50, message = "Name must not exceed 50 characters")
                String name) {}
