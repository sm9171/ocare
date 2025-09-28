package com.health.app.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.health.app.application.port.out.LoadUserPort;
import com.health.app.application.port.out.SaveUserPort;
import com.health.app.domain.common.RecordKey;
import com.health.app.domain.user.User;

@Component
public class UserPersistenceAdapter implements LoadUserPort, SaveUserPort {

    private final UserJpaRepository userJpaRepository;

    public UserPersistenceAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<User> loadUserById(Long id) {
        return userJpaRepository.findById(id).map(UserJpaEntity::toDomain);
    }

    @Override
    public Optional<User> loadUserByRecordKey(RecordKey recordKey) {
        return userJpaRepository.findByRecordKey(recordKey.getValue()).map(UserJpaEntity::toDomain);
    }

    @Override
    public Optional<User> loadUserByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(UserJpaEntity::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public User saveUser(User user) {
        UserJpaEntity entity = UserJpaEntity.fromDomain(user);
        UserJpaEntity savedEntity = userJpaRepository.save(entity);
        return savedEntity.toDomain();
    }
}
