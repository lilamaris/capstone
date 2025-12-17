package com.lilamaris.capstone.adapter.in.security.authn.handler;

import com.lilamaris.capstone.adapter.in.security.util.ResponseWriter;
import com.lilamaris.capstone.application.port.in.result.AuthResult;
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
