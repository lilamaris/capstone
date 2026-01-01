package com.lilamaris.capstone.orchestration.auth.resolver;

import com.lilamaris.capstone.orchestration.auth.result.AuthResult;

public interface SessionIssuer {
    AuthResult.Token issue(AuthIdentity identity);
}
