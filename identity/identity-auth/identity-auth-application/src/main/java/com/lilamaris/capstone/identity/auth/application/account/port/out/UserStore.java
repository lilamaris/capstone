package com.lilamaris.capstone.identity.auth.application.account.port.out;

import com.lilamaris.capstone.identity.auth.domain.User;

public interface UserStore {
    User save(User user);

    void delete(User user);
}
