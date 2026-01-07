package com.lilamaris.capstone.scenario.auth.infrastructure.security.authz.jwt.handler;

import com.lilamaris.capstone.scenario.auth.infrastructure.security.util.ResponseWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ResponseWriter writer;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {
        var e = (Exception) request.getAttribute("jwtException");
        writer.error(response, HttpStatus.UNAUTHORIZED, e != null ? e.getMessage() : "Failed.");
    }
}
