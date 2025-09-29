package com.health.app.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.health.app.adapter.out.persistence")
@EntityScan(basePackages = "com.health.app.adapter.out.persistence")
public class ApplicationConfiguration {
    // This configuration ensures that JPA repositories and entities are properly scanned
    // and that the hexagonal architecture components are wired correctly
}