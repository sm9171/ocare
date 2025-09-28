package com.health.app.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.health.app.adapter.in.web.dto.RegisterUserRequest;
import com.health.app.adapter.in.web.dto.UserResponse;
import com.health.app.application.port.in.RegisterUserCommand;
import com.health.app.application.port.in.RegisterUserUseCase;
import com.health.app.domain.user.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;

    public UserController(RegisterUserUseCase registerUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody RegisterUserRequest request) {
        RegisterUserCommand command =
                new RegisterUserCommand(request.email(), request.password(), request.name());

        User user = registerUserUseCase.registerUser(command);
        UserResponse response = UserResponse.from(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
