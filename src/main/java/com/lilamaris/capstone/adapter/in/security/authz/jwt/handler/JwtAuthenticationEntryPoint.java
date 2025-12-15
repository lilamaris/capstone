package com.lilamaris.capstone.adapter.in.security.authz.jwt.handler;

import com.lilamaris.capstone.adapter.in.security.util.ResponseWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

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
        var e = (Exception) Optional.ofNullable(request.getAttribute("jwtException")).orElse(authException);
        writer.sendError(response, HttpStatus.UNAUTHORIZED, e.getMessage());
    }
}
