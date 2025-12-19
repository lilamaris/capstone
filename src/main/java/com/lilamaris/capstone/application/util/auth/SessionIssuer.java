package com.lilamaris.capstone.application.util.auth;

import com.lilamaris.capstone.application.port.in.result.AuthResult;

public interface SessionIssuer {
    AuthResult.Token issue(AuthIdentity identity);
}
