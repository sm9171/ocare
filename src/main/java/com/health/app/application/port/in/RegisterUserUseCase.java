package com.health.app.application.port.in;

import com.health.app.domain.user.User;

public interface RegisterUserUseCase {

    User registerUser(RegisterUserCommand command);
}
