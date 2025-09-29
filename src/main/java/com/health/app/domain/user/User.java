package com.health.app.domain.user;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

import com.health.app.domain.common.RecordKey;

public class User {

    public static final Random RANDOM = new Random();
    private RecordKey recordKey;
    private String email;
    private String password;
    private String name;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long id;

    protected User() {}

    public User(String email, String password, String name) {
        this.recordKey = new RecordKey(generateRecordKey());
        this.email = validateEmail(email);
        this.password = validatePassword(password);
        this.name = validateName(name);
        this.status = UserStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public User(
            Long id,
            RecordKey recordKey,
            String email,
            String password,
            String name,
            UserStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.recordKey = recordKey;
        this.email = email;
        this.password = password;
        this.name = name;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        return email.trim().toLowerCase();
    }

    private String validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }
        return password;
    }

    private String validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        return name.trim();
    }

    private String generateRecordKey() {
        return "USER_" + System.currentTimeMillis() + "_" + RANDOM.nextInt(1000);
    }

    public RecordKey getRecordKey() {
        return recordKey;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public UserStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(recordKey, user.recordKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordKey);
    }
}
