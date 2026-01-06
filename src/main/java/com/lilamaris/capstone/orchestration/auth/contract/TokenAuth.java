package com.lilamaris.capstone.orchestration.auth.contract;

import com.lilamaris.capstone.orchestration.auth.result.AuthResult;
import io.jsonwebtoken.Claims;

public interface TokenAuth {
    AuthResult.Token reissue(String refreshToken);

    Claims parseToken(String token);
}
