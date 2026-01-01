package com.lilamaris.capstone.orchestration.auth.contract;

import com.lilamaris.capstone.orchestration.auth.result.AuthResult;

public interface TokenAuth {
    AuthResult.Token reissue(String refreshToken);
}
