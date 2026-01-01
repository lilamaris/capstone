package com.lilamaris.capstone.shared.infrastructure.security.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lilamaris.capstone.orchestration.auth.result.AuthResult;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ResponseWriter {
    private final ObjectMapper mapper;

    public void error(HttpServletResponse response, HttpStatus code, String message) throws IOException {
        response.setStatus(code.value());
        var body = Map.of("error", message);
        write(response, body);
    }

    public void token(HttpServletResponse response, AuthResult.Token result) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        write(response, result);
    }

    private void write(HttpServletResponse response, Object body) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(mapper.writeValueAsString(body));
    }
}
