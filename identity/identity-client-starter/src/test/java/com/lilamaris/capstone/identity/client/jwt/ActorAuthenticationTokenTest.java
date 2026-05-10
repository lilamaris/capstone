package com.lilamaris.capstone.identity.client.jwt;

import com.lilamaris.capstone.identity.core.actor.Actor;
import com.lilamaris.capstone.identity.core.actor.SimpleActor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ActorAuthenticationToken 테스트")
class ActorAuthenticationTokenTest {

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {
        @Test
        @DisplayName("actor와 jwt를 principal, credentials로 보관하고 인증 완료 상태가 된다")
        void create_authenticated_token_with_actor_and_jwt() {
            // given
            Actor actor = SimpleActor.of("user-1", Set.of());
            var jwt = jwt();

            // when
            var token = new ActorAuthenticationToken(
                    actor,
                    jwt,
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );

            // then
            assertThat(token.isAuthenticated()).isTrue();
            assertThat(token.getPrincipal()).isEqualTo(actor);
            assertThat(token.getCredentials()).isEqualTo(jwt);
            assertThat(token.getName()).isEqualTo("user-1");
            assertThat(token.getAuthorities())
                    .extracting("authority")
                    .containsExactly("ROLE_USER");
        }
    }

    private Jwt jwt() {
        return Jwt.withTokenValue("access-token")
                .header("alg", "none")
                .subject("user-1")
                .build();
    }
}
