package com.lilamaris.capstone.adapter.in.security.authz.jwt;

import com.lilamaris.capstone.adapter.in.security.ResponseWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        var exception = (Exception) request.getAttribute("exception");
        ResponseWriter.sendError(response, HttpStatus.UNAUTHORIZED, exception.getMessage());
    }
}
