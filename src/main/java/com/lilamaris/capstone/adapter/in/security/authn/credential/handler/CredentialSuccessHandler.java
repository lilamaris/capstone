package com.lilamaris.capstone.adapter.in.security.authn.credential.handler;

import com.lilamaris.capstone.adapter.in.security.util.ResponseWriter;
import com.lilamaris.capstone.adapter.in.security.SecurityUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CredentialSuccessHandler implements AuthenticationSuccessHandler {
    private final ResponseWriter writer;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        SecurityUserDetails principal = (SecurityUserDetails) authentication.getPrincipal();
        writer.sendToken(response, principal);
    }
}
