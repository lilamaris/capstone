package com.lilamaris.capstone.identity.auth.security.credential.filter;

import com.lilamaris.capstone.identity.auth.security.credential.request.CredentialSignInRequest;
import com.lilamaris.capstone.identity.auth.security.exception.AuthenticationProcessingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonCredentialSignInProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private final ObjectMapper objectMapper;

    public JsonCredentialSignInProcessingFilter(RequestMatcher matcher, ObjectMapper objectMapper) {
        super(matcher);
        this.objectMapper = objectMapper;
    }

    @Override
    public @Nullable Authentication attemptAuthentication(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response
    ) throws AuthenticationException, IOException, ServletException {
        final CredentialSignInAuthentication authentication;
        try {
            var body = objectMapper.readValue(request.getInputStream(), CredentialSignInRequest.class);
            authentication = CredentialSignInAuthentication.of(body.email(), body.password());
        } catch (Exception e) {
            throw new AuthenticationProcessingException("Invalid credential sign-in request.");
        }

        return getAuthenticationManager().authenticate(authentication);
    }
}
