package com.health.app.domain.user;

import java.util.Optional;

import com.health.app.domain.common.RecordKey;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByRecordKey(RecordKey recordKey);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    void delete(User user);
}
