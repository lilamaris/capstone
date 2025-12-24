package com.lilamaris.capstone.application.port.out;

import com.lilamaris.capstone.domain.model.capstone.user.User;
import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;

import java.util.Optional;

public interface UserPort {
    Optional<User> getById(UserId id);

    User save(User domain);
}
