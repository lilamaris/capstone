package com.lilamaris.capstone.adapter.in.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ResponseWriter {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void sendError(HttpServletResponse response, HttpStatus code, String message) throws IOException {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", false);
        responseBody.put("data", null);
        responseBody.put("error", Map.of("message", message));

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(code.value());
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }

    public static void sendToken(HttpServletResponse response, String accessToken, String refreshToken) throws IOException {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", false);
        responseBody.put("data", Map.of("access_token", accessToken, "refresh_token", refreshToken));

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }
}
