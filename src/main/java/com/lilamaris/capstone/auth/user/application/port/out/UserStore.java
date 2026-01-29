package com.lilamaris.capstone.auth.user.application.port.out;

import com.lilamaris.capstone.auth.user.domain.User;
import com.lilamaris.capstone.auth.user.domain.id.UserId;

import java.util.Optional;

public interface UserStore {
    Optional<User> getById(UserId id);

    boolean isExists(UserId id);

    User save(User domain);
}
