package com.lilamaris.capstone.identity.auth.persistence.account.jpa;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootConfiguration
@EnableAutoConfiguration
@EntityScan(basePackages = "com.lilamaris.capstone.identity.auth.domain.account")
@EnableJpaRepositories(basePackages = "com.lilamaris.capstone.identity.auth.persistence.account.jpa.repository")
class JpaPersistenceAdapterTestConfig {
}
