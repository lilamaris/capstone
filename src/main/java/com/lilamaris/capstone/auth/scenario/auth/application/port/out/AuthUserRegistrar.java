package com.lilamaris.capstone.auth.scenario.auth.application.port.out;

public interface AuthUserRegistrar {
    AuthUserEntry register(String displayName);
}
