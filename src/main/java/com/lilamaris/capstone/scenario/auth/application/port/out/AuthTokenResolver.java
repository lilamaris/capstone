package com.lilamaris.capstone.scenario.auth.application.port.out;

public interface AuthTokenResolver {
    AuthRefreshTokenConsumeEntry resolve(String token);
}
