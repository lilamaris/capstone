package com.lilamaris.capstone.auth.scenario.auth.application.port.out;

public interface AuthTokenResolver {
    AuthRefreshTokenConsumeEntry resolve(String token);
}
