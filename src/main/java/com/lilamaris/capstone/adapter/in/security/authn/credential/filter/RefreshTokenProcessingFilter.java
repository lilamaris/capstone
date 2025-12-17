package com.lilamaris.capstone.adapter.in.security.authn.credential.filter;

import com.lilamaris.capstone.adapter.in.security.authn.credential.token.RefreshTokenAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

public class RefreshTokenProcessingFilter extends AbstractAuthenticationProcessingFilter {
    private static final RequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = PathPatternRequestMatcher.withDefaults()
            .matcher(HttpMethod.POST, "/api/v1/auth/refresh");

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    public RefreshTokenProcessingFilter() {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {
        String authorization = request.getHeader(AUTHORIZATION_HEADER);

        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            throw new AuthenticationServiceException("Refresh token is missing");
        }

        String refreshToken = authorization.substring(BEARER_PREFIX.length());

        if (!StringUtils.hasText(refreshToken)) {
            throw new AuthenticationServiceException("Refresh token is empty");
        }

        var token = new RefreshTokenAuthenticationToken(refreshToken);

        return this.getAuthenticationManager().authenticate(token);
    }
}
