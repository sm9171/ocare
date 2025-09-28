package com.health.app.adapter.out.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {

    Optional<UserJpaEntity> findByRecordKey(String recordKey);

    Optional<UserJpaEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
