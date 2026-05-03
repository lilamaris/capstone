package com.lilamaris.capstone.identity.auth.persistence.account.repository;

import com.lilamaris.capstone.identity.auth.domain.account.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
