package com.lilamaris.capstone.auth.application.resolver;

import com.lilamaris.capstone.auth.application.result.AuthResult;

public interface SessionIssuer {
    AuthResult.Token issue(AuthIdentity identity);
}
