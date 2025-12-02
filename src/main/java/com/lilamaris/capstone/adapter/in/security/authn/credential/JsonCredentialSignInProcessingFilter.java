package com.lilamaris.capstone.adapter.in.security.authn.credential;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsonCredentialSignInProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private static final RequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = PathPatternRequestMatcher.withDefaults()
            .matcher(HttpMethod.POST, "/api/v1/auth/signIn");

    private final ObjectMapper mapper;

    public JsonCredentialSignInProcessingFilter(ObjectMapper mapper) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
        this.mapper = mapper;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException, IOException, ServletException {
        try {
            var signInRequest = mapper.readValue(request.getInputStream(), CredentialRequest.SignIn.class);

            var authToken = new UsernamePasswordAuthenticationToken(
                    signInRequest.email(),
                    signInRequest.password()
            );

            return this.getAuthenticationManager().authenticate(authToken);
        } catch (IOException e) {
            throw new AuthenticationServiceException("Invalid Request Body");
        }
    }
}
