package com.lilamaris.capstone.scenario.auth.application.resolver;

import com.lilamaris.capstone.scenario.auth.application.result.AuthResult;

public interface SessionIssuer {
    AuthResult.Token issue(AuthIdentity identity);
}
