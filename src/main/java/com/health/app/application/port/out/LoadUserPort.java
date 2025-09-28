package com.health.app.application.port.out;

import java.util.Optional;

import com.health.app.domain.common.RecordKey;
import com.health.app.domain.user.User;

public interface LoadUserPort {

    Optional<User> loadUserById(Long id);

    Optional<User> loadUserByRecordKey(RecordKey recordKey);

    Optional<User> loadUserByEmail(String email);

    boolean existsByEmail(String email);
}
