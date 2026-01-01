package com.lilamaris.capstone.shared.infrastructure.security.authz.jwt;

import com.lilamaris.capstone.shared.application.context.ActorContext;
import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.shared.domain.event.actor.UserActor;
import com.lilamaris.capstone.shared.infrastructure.security.SecurityUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ActorContextFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                var principal = (SecurityUserDetails) auth.getPrincipal();
                CanonicalActor actor = UserActor.of(principal.getUserId().toString());
                ActorContext.set(actor);
            }

            filterChain.doFilter(request, response);
        } finally {
            ActorContext.clear();
        }
    }
}
