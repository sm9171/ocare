package com.health.app.domain.user;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void createUser_ValidData_Success() {
        // given
        String email = "test@example.com";
        String password = "password123";
        String name = "Test User";

        // when
        User user = new User(email, password, name);

        // then
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getRecordKey()).isNotNull();
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getUpdatedAt()).isNotNull();
    }

    @Test
    void createUser_InvalidEmail_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> new User("invalid-email", "password123", "Test User"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid email format");
    }

    @Test
    void createUser_ShortPassword_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> new User("test@example.com", "short", "Test User"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Password must be at least 8 characters");
    }

    @Test
    void createUser_EmptyName_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> new User("test@example.com", "password123", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Name cannot be null or empty");
    }


    @Test
    void equals_SameRecordKey_ReturnsTrue() {
        // given
        User user1 = new User("test1@example.com", "password123", "User 1");
        User user2 = new User("test2@example.com", "password456", "User 2");

        // 같은 RecordKey로 설정 (실제로는 시스템에서 생성되지만 테스트를 위해)
        user2 =
                new User(
                        user2.getId(),
                        user1.getRecordKey(),
                        user2.getEmail(),
                        user2.getPassword(),
                        user2.getName(),
                        user2.getStatus(),
                        user2.getCreatedAt(),
                        user2.getUpdatedAt());

        // when & then
        assertThat(user1).isEqualTo(user2);
    }
}
