package com.lilamaris.capstone.identity.auth.webmvc.role.controller;

import com.lilamaris.capstone.identity.auth.application.role.port.in.GrantRoleUseCase;
import com.lilamaris.capstone.identity.auth.application.role.port.in.ListUserGrantedRoleUseCase;
import com.lilamaris.capstone.identity.auth.application.role.port.in.RevokeRoleUseCase;
import com.lilamaris.capstone.identity.auth.application.role.port.in.result.UserGrantedRoleResult;
import com.lilamaris.capstone.identity.core.actor.Actor;
import com.lilamaris.capstone.identity.core.actor.context.ActorContextHolder;
import com.lilamaris.capstone.identity.core.role.CanonicalRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.NestedTestConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("unit")
@SpringBootTest(classes = RoleControllerTest.TestApplication.class)
@AutoConfigureMockMvc
@NestedTestConfiguration(NestedTestConfiguration.EnclosingConfiguration.INHERIT)
@DisplayName("RoleController 테스트")
class RoleControllerTest {
    private static final UUID USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final Instant CREATED_AT = Instant.parse("2026-01-01T00:00:00Z");

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    GrantRoleUseCase grantRoleUseCase;

    @MockitoBean
    RevokeRoleUseCase revokeRoleUseCase;

    @MockitoBean
    ListUserGrantedRoleUseCase listUserGrantedRoleUseCase;

    @MockitoBean
    ActorContextHolder actorContextHolder;

    @MockitoBean
    Actor actor;

    @Configuration
    @EnableMethodSecurity
    static class MethodSecurityTestConfig {
        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            return http
                    .authorizeHttpRequests(registry -> registry.anyRequest().authenticated())
                    .build();
        }
    }

    @SpringBootConfiguration
    @EnableAutoConfiguration
    @Import({RoleController.class, MethodSecurityTestConfig.class})
    static class TestApplication {
    }

    @Nested
    @DisplayName("권한 부여")
    class GrantRoleTest {
        @Test
        @DisplayName("role.grant authority가 있으면 통과한다")
        void pass_when_has_grant_role_authority() throws Exception {
            when(actorContextHolder.getActor()).thenReturn(actor);
            when(grantRoleUseCase.grant(any()))
                    .thenReturn(new UserGrantedRoleResult(USER_ID, "identity-auth", CanonicalRole.USER, CREATED_AT));

            mockMvc.perform(post("/roles")
                            .with(csrf())
                            .with(user("tester").authorities(() -> "role.grant"))
                            .contentType("application/json")
                            .content("""
                                    {
                                      "userId": "00000000-0000-0000-0000-000000000001",
                                      "namespaceName": "identity-auth",
                                      "role": "USER"
                                    }
                                    """))
                    .andExpect(status().isOk());

            verify(grantRoleUseCase).grant(any());
        }

        @Test
        @DisplayName("role.grant authority가 없으면 거부한다")
        void reject_when_has_no_grant_role_authority() throws Exception {
            mockMvc.perform(post("/roles")
                            .with(csrf())
                            .with(user("tester").authorities(() -> "role.revoke"))
                            .contentType("application/json")
                            .content("""
                                    {
                                      "userId": "00000000-0000-0000-0000-000000000001",
                                      "namespaceName": "identity-auth",
                                      "role": "USER"
                                    }
                                    """))
                    .andExpect(status().isForbidden());

            verify(grantRoleUseCase, never()).grant(any());
        }
    }

    @Nested
    @DisplayName("권한 회수")
    class RevokeRoleTest {
        @Test
        @DisplayName("role.revoke authority가 있으면 통과한다")
        void pass_when_has_revoke_role_authority() throws Exception {
            when(actorContextHolder.getActor()).thenReturn(actor);

            mockMvc.perform(delete("/roles")
                            .with(csrf())
                            .with(user("tester").authorities(() -> "role.revoke"))
                            .contentType("application/json")
                            .content("""
                                    {
                                      "userId": "00000000-0000-0000-0000-000000000001",
                                      "namespaceName": "identity-auth",
                                      "role": "USER"
                                    }
                                    """))
                    .andExpect(status().isNoContent());

            verify(revokeRoleUseCase).revoke(any());
        }

        @Test
        @DisplayName("role.revoke authority가 없으면 거부한다")
        void reject_when_has_no_revoke_role_authority() throws Exception {
            mockMvc.perform(delete("/roles")
                            .with(csrf())
                            .with(user("tester").authorities(() -> "role.grant"))
                            .contentType("application/json")
                            .content("""
                                    {
                                      "userId": "00000000-0000-0000-0000-000000000001",
                                      "namespaceName": "identity-auth",
                                      "role": "USER"
                                    }
                                    """))
                    .andExpect(status().isForbidden());

            verify(revokeRoleUseCase, never()).revoke(any());
        }
    }
}
