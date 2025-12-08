package com.lilamaris.capstone.adapter.in.security.authn.credential.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lilamaris.capstone.adapter.in.security.authn.credential.CredentialRequest;
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
    ) throws AuthenticationException  {
        UsernamePasswordAuthenticationToken token;
        try {
            var signInRequest = mapper.readValue(request.getInputStream(), CredentialRequest.SignIn.class);
            token = new UsernamePasswordAuthenticationToken(
                    signInRequest.email(),
                    signInRequest.password()
            );
        } catch (Exception e) {
            throw new AuthenticationServiceException("Invalid Request Body");
        }

        return this.getAuthenticationManager().authenticate(token);
    }
}
