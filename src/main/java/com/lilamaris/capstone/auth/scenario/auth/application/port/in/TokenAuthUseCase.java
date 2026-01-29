package com.lilamaris.capstone.auth.scenario.auth.application.port.in;

import com.lilamaris.capstone.auth.scenario.auth.application.result.AuthResult;
import io.jsonwebtoken.Claims;

public interface TokenAuthUseCase {
    AuthResult.Token reissue(String refreshToken);

    Claims parseToken(String token);
}
