package com.health.app.adapter.in.web.dto;

import java.time.LocalDateTime;

import com.health.app.domain.user.User;
import com.health.app.domain.user.UserStatus;

public record UserResponse(
        Long id,
        String recordKey,
        String email,
        String name,
        UserStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getRecordKey().getValue(),
                user.getEmail(),
                user.getName(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }
}
