package com.lilamaris.capstone.adapter.in.security.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lilamaris.capstone.adapter.in.security.SecurityUserDetails;
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
    private final JwtUtil jwtUtil;

    public void sendError(HttpServletResponse response, HttpStatus code, String message) throws IOException {
        var body = Map.of(
                "success", false,
                "data", null,
                "error", Map.of("message", message)
        );

        write(response, code, body);
    }

    public void sendToken(HttpServletResponse response, SecurityUserDetails principal) throws IOException {
        String accessToken = jwtUtil.createAccessToken(principal);
        String refreshToken = jwtUtil.createRefreshToken(principal);

        var body = Map.of(
                "success", true,
                "data", Map.of(
                        "access_token", accessToken,
                        "refresh_token", refreshToken
                )
        );

        write(response, HttpStatus.OK, body);
    }

    private void write(HttpServletResponse response, HttpStatus code, Object body) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(code.value());
        response.getWriter().write(mapper.writeValueAsString(body));
    }
}
