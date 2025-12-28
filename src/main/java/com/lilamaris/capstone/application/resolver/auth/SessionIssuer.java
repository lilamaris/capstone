package com.lilamaris.capstone.application.resolver.auth;

import com.lilamaris.capstone.application.port.in.result.AuthResult;

public interface SessionIssuer {
    AuthResult.Token issue(AuthIdentity identity);
}
