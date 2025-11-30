package com.lilamaris.capstone.adapter.in.security.authz.jwt;

import com.lilamaris.capstone.adapter.in.security.SecurityUserDetailsMapper;
import com.lilamaris.capstone.adapter.in.security.exception.SecurityException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTH_HEADER = "Authorization";
    private static final String AUTH_HEADER_PREFIX = "Bearer ";

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            var token = resolveToken(request);

            if (StringUtils.hasText(token)) {
                var claims = jwtProvider.parseToken(token);
                var principal = SecurityUserDetailsMapper.fromClaims(claims);
                var authentication = new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (SecurityException e) {
            request.setAttribute("exception", e);
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTH_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTH_HEADER_PREFIX)) {
            return bearerToken.substring(AUTH_HEADER_PREFIX.length());
        }
        return null;
    }
}
