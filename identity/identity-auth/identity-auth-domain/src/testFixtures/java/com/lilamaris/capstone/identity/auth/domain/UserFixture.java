package com.lilamaris.capstone.identity.auth.domain;

import com.lilamaris.capstone.identity.auth.domain.account.User;

import java.time.Instant;

public class UserFixture {
    public static final String INITIAL_NICKNAME = "tester";

    public static User createUser(Instant createdAt) {
        return User.of(INITIAL_NICKNAME, createdAt);
    }
}