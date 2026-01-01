package com.lilamaris.capstone.user.application.port.out;

import com.lilamaris.capstone.user.domain.User;
import com.lilamaris.capstone.user.domain.id.UserId;

import java.util.Optional;

public interface UserPort {
    Optional<User> getById(UserId id);

    User save(User domain);
}
