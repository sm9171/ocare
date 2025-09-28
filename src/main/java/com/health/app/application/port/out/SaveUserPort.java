package com.health.app.application.port.out;

import com.health.app.domain.user.User;

public interface SaveUserPort {

    User saveUser(User user);
}
