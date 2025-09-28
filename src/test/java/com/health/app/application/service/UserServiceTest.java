package com.health.app.application.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.health.app.application.port.in.RegisterUserCommand;
import com.health.app.application.port.out.LoadUserPort;
import com.health.app.application.port.out.SaveUserPort;
import com.health.app.domain.user.User;

class UserServiceTest {

    @Mock private LoadUserPort loadUserPort;

    @Mock private SaveUserPort saveUserPort;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(loadUserPort, saveUserPort);
    }

    @Test
    void registerUser_ValidCommand_Success() {
        // given
        RegisterUserCommand command =
                new RegisterUserCommand("test@example.com", "password123", "Test User");

        User savedUser = new User("test@example.com", "password123", "Test User");

        when(loadUserPort.existsByEmail("test@example.com")).thenReturn(false);
        when(saveUserPort.saveUser(any(User.class))).thenReturn(savedUser);

        // when
        User result = userService.registerUser(command);

        // then
        assertThat(result).isEqualTo(savedUser);
        verify(loadUserPort).existsByEmail("test@example.com");
        verify(saveUserPort).saveUser(any(User.class));
    }

    @Test
    void registerUser_EmailAlreadyExists_ThrowsException() {
        // given
        RegisterUserCommand command =
                new RegisterUserCommand("test@example.com", "password123", "Test User");

        when(loadUserPort.existsByEmail("test@example.com")).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.registerUser(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email already exists: test@example.com");

        verify(loadUserPort).existsByEmail("test@example.com");
        verify(saveUserPort, never()).saveUser(any(User.class));
    }
}
