package com.lilamaris.capstone.identity.client.jwt;

import com.lilamaris.capstone.identity.core.actor.Actor;
import com.lilamaris.capstone.identity.core.actor.context.ActorContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class ActorContextBindingFilter extends OncePerRequestFilter {
    private final ActorContextHolder contextHolder;

    @NullMarked
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null
                && auth.isAuthenticated()
                && auth.getPrincipal() instanceof Actor actor) {
            contextHolder.setActor(actor);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            contextHolder.clear();
        }
    }
}
