package com.lilamaris.capstone.scenario.auth.application.port.in;

import com.lilamaris.capstone.scenario.auth.application.result.AuthResult;
import io.jsonwebtoken.Claims;

public interface TokenAuthUseCase {
    AuthResult.Token reissue(String refreshToken);

    Claims parseToken(String token);
}
