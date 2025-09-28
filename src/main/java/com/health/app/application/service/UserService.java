package com.health.app.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.health.app.application.port.in.RegisterUserCommand;
import com.health.app.application.port.in.RegisterUserUseCase;
import com.health.app.application.port.out.LoadUserPort;
import com.health.app.application.port.out.SaveUserPort;
import com.health.app.domain.user.User;

@Service
@Transactional
public class UserService implements RegisterUserUseCase {

    private final LoadUserPort loadUserPort;
    private final SaveUserPort saveUserPort;

    public UserService(LoadUserPort loadUserPort, SaveUserPort saveUserPort) {
        this.loadUserPort = loadUserPort;
        this.saveUserPort = saveUserPort;
    }

    @Override
    public User registerUser(RegisterUserCommand command) {
        validateEmailNotExists(command.email());

        User user = new User(command.email(), command.password(), command.name());

        return saveUserPort.saveUser(user);
    }

    private void validateEmailNotExists(String email) {
        if (loadUserPort.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists: " + email);
        }
    }
}
