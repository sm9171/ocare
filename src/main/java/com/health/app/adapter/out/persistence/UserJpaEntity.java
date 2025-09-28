package com.health.app.adapter.out.persistence;

import java.time.LocalDateTime;

import com.health.app.domain.common.RecordKey;
import com.health.app.domain.user.User;
import com.health.app.domain.user.UserStatus;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_key", nullable = false, unique = true, length = 100)
    private String recordKey;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private UserStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected UserJpaEntity() {}

    public UserJpaEntity(
            String recordKey,
            String email,
            String password,
            String name,
            UserStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.recordKey = recordKey;
        this.email = email;
        this.password = password;
        this.name = name;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserJpaEntity fromDomain(User user) {
        return new UserJpaEntity(
                user.getRecordKey().getValue(),
                user.getEmail(),
                user.getPassword(),
                user.getName(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }

    public User toDomain() {
        return new User(
                this.id,
                new RecordKey(this.recordKey),
                this.email,
                this.password,
                this.name,
                this.status,
                this.createdAt,
                this.updatedAt);
    }


    public Long getId() {
        return id;
    }

    public String getRecordKey() {
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
}
