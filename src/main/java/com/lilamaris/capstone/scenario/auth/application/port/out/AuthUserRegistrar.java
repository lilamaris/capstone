package com.lilamaris.capstone.scenario.auth.application.port.out;

public interface AuthUserRegistrar {
    AuthUserEntry register(String displayName);
}
