package com.lilamaris.capstone.auth.scenario.auth.application.port.out;

public interface AuthAccountExistenceChecker {
    boolean isExists(AuthProvider authProvider, String principalId);
}
