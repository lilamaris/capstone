package com.lilamaris.capstone.scenario.auth.application.port.out;

public interface AuthAccountExistenceChecker {
    boolean isExists(AuthProvider authProvider, String principalId);
}
