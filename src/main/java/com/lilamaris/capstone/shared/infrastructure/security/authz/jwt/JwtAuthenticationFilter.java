package com.lilamaris.capstone.shared.infrastructure.security.authz.jwt;

import com.lilamaris.capstone.orchestration.auth.contract.TokenAuth;
import com.lilamaris.capstone.shared.application.context.ActorContext;
import com.lilamaris.capstone.shared.application.exception.ApplicationInvariantException;
import com.lilamaris.capstone.shared.domain.event.actor.UserActor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String AUTH_HEADER = "Authorization";
    private static final String AUTH_HEADER_PREFIX = "Bearer ";

    private final TokenAuth tokenAuth;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            var token = resolveToken(request);

            if (StringUtils.hasText(token)) {
                var claims = tokenAuth.parseToken(token);
                var actor = UserActor.of(claims.getSubject());
                var authorities = Collections.singletonList(new SimpleGrantedAuthority(actor.type().name()));
                var authentication = new UsernamePasswordAuthenticationToken(actor, token, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                ActorContext.set(actor);
            }
        } catch (ApplicationInvariantException e) {
            request.setAttribute("jwtException", e);
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
