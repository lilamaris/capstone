package com.lilamaris.capstone.scenario.auth.application.port.out;

public interface AuthUserRegistrar {
    UserEntry register(String displayName);
}
