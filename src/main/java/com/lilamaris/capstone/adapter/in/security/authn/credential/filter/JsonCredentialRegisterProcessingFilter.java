package com.lilamaris.capstone.adapter.in.security.authn.credential.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lilamaris.capstone.adapter.in.security.authn.credential.CredentialRequest;
import com.lilamaris.capstone.adapter.in.security.authn.credential.provider.RegisterAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsonCredentialRegisterProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private static final RequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = PathPatternRequestMatcher.withDefaults()
            .matcher(HttpMethod.POST, "/api/v1/auth/register");

    private final ObjectMapper mapper;

    public JsonCredentialRegisterProcessingFilter(ObjectMapper mapper) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        this.mapper = mapper;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {
        try {
            var registerRequest = mapper.readValue(request.getInputStream(), CredentialRequest.Register.class);

            var token = new RegisterAuthenticationToken(
                    registerRequest.email(),
                    registerRequest.password(),
                    registerRequest.displayName()
            );

            return this.getAuthenticationManager().authenticate(token);
        } catch (IOException e) {
            throw new AuthenticationServiceException("Invalid Request Body");
        }
    }
}
