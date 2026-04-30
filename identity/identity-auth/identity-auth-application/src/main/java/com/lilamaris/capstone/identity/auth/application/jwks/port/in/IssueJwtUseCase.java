package com.lilamaris.capstone.identity.auth.application.jwks.port.in;

import java.util.Set;

public interface IssueJwtUseCase {
    String issue(String subject, Set<String> scopes);
}
