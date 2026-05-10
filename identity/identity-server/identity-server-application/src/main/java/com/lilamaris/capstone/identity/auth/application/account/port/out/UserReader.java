package com.lilamaris.capstone.identity.auth.application.account.port.out;

import com.lilamaris.capstone.identity.auth.domain.account.User;

import java.util.Optional;
import java.util.UUID;

public interface UserReader {
    boolean existsById(UUID id);

    Optional<User> findById(UUID id);
}
