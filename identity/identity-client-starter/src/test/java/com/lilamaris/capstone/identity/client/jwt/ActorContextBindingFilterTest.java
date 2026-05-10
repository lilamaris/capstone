package com.lilamaris.capstone.identity.client.jwt;

import com.lilamaris.capstone.identity.core.actor.Actor;
import com.lilamaris.capstone.identity.core.actor.SimpleActor;
import com.lilamaris.capstone.identity.core.actor.context.ActorContextHolder;
import com.lilamaris.capstone.identity.core.actor.context.ThreadLocalActorContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ActorContextBindingFilter 테스트")
class ActorContextBindingFilterTest {
    ActorContextHolder contextHolder = new ThreadLocalActorContextHolder();
    ActorContextBindingFilter filter = new ActorContextBindingFilter(contextHolder);

    @AfterEach
    void clear() {
        contextHolder.clear();
        SecurityContextHolder.clearContext();
    }

    @Nested
    @DisplayName("필터 테스트")
    class FilterTest {
        @Test
        @DisplayName("인증된 principal이 actor이면 요청 처리 동안 ActorContextHolder에 바인딩한다")
        void bind_actor_context_during_filter_chain_when_authenticated_principal_is_actor() throws ServletException, IOException {
            // given
            Actor actor = SimpleActor.of("user-1", Set.of());
            var authentication = UsernamePasswordAuthenticationToken.authenticated(actor, "credentials", List.of());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            var boundActor = new AtomicReference<Actor>();
            FilterChain filterChain = (request, response) -> boundActor.set(contextHolder.getActor());

            // when
            filter.doFilter(new MockHttpServletRequest(), new MockHttpServletResponse(), filterChain);

            // then
            assertThat(boundActor.get()).isEqualTo(actor);
            assertThat(contextHolder.getActor()).isNull();
        }

        @Test
        @DisplayName("principal이 actor가 아니면 ActorContextHolder에 바인딩하지 않는다")
        void does_not_bind_actor_context_when_principal_is_not_actor() throws ServletException, IOException {
            // given
            var authentication = UsernamePasswordAuthenticationToken.authenticated("user-1", "credentials", List.of());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            var boundActor = new AtomicReference<Actor>();
            FilterChain filterChain = (request, response) -> boundActor.set(contextHolder.getActor());

            // when
            filter.doFilter(new MockHttpServletRequest(), new MockHttpServletResponse(), filterChain);

            // then
            assertThat(boundActor.get()).isNull();
            assertThat(contextHolder.getActor()).isNull();
        }

        @Test
        @DisplayName("요청 처리 중 예외가 발생해도 ActorContextHolder를 clear한다")
        void clear_actor_context_even_when_filter_chain_throws_exception() {
            // given
            Actor actor = SimpleActor.of("user-1", Set.of());
            var authentication = UsernamePasswordAuthenticationToken.authenticated(actor, "credentials", List.of());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            FilterChain filterChain = (request, response) -> {
                throw new ServletException("filter failed");
            };

            // when
            try {
                filter.doFilter(new MockHttpServletRequest(), new MockHttpServletResponse(), filterChain);
            } catch (ServletException | IOException ignored) {
            }

            // then
            assertThat(contextHolder.getActor()).isNull();
        }
    }
}
