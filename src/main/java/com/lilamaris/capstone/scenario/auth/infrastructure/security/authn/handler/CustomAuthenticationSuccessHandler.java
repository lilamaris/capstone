package com.lilamaris.capstone.scenario.auth.infrastructure.security.authn.handler;

import com.lilamaris.capstone.scenario.auth.application.result.AuthResult;
import com.lilamaris.capstone.scenario.auth.infrastructure.security.util.ResponseWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final ResponseWriter writer;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        var tokenResult = (AuthResult.Token) authentication.getDetails();
        writer.token(response, tokenResult);
    }
}
