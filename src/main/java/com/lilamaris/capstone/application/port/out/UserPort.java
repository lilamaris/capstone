package com.lilamaris.capstone.application.port.out;

import com.lilamaris.capstone.domain.user.User;

import java.util.Optional;

public interface UserPort {
    Optional<User> getById(User.Id id);

    User save(User domain);
}
