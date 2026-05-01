package com.lilamaris.capstone.identity.auth.application.role.internal;

import com.lilamaris.capstone.identity.auth.application.role.RoleUseCaseTestSupport;
import com.lilamaris.capstone.identity.auth.application.role.port.out.UserGrantedRoleStore;
import com.lilamaris.capstone.identity.auth.domain.role.UserGrantedRole;
import com.lilamaris.capstone.identity.core.role.CanonicalRole;
import com.lilamaris.capstone.identity.core.role.InitialUserGrantedRoleRegistry;
import com.lilamaris.capstone.identity.core.role.SimpleNamespaceRole;
import com.lilamaris.capstone.kernel.core.namespace.SimpleApplicationNamespace;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
@DisplayName("InitialUserGrantedRoleProvisioner 테스트")
class InitialUserGrantedRoleProvisionerTest {
    @Mock
    InitialUserGrantedRoleRegistry registry;

    @Mock
    UserGrantedRoleStore store;

    InitialUserGrantedRoleProvisioner provisioner;

    @BeforeEach
    void setUp() {
        provisioner = new InitialUserGrantedRoleProvisioner(
                registry,
                store,
                RoleUseCaseTestSupport.CLOCK
        );
    }

    @Nested
    @DisplayName("초기 권한 부여")
    class GrantTest {
        @Test
        @DisplayName("registry의 초기 권한을 사용자 권한으로 저장한다")
        void save_initial_user_granted_roles() {
            var identityRole = SimpleNamespaceRole.of(SimpleApplicationNamespace.of("identity"), CanonicalRole.USER);
            var timelineRole = SimpleNamespaceRole.of(SimpleApplicationNamespace.of("timeline"), CanonicalRole.GUEST);
            when(registry.getAll()).thenReturn(List.of(identityRole, timelineRole));

            var grantedRoles = provisioner.grant(RoleUseCaseTestSupport.USER_ID);

            @SuppressWarnings("unchecked")
            ArgumentCaptor<Collection<UserGrantedRole>> captor = ArgumentCaptor.forClass(Collection.class);
            verify(store).saveAll(captor.capture());

            assertThat(grantedRoles)
                    .hasSize(2)
                    .extracting(role -> role.getNamespace().name())
                    .containsExactlyInAnyOrder("identity", "timeline");
            assertThat(captor.getValue()).containsExactlyElementsOf(grantedRoles);
            assertThat(grantedRoles)
                    .allSatisfy(role -> {
                        assertThat(role.getUserId()).isEqualTo(RoleUseCaseTestSupport.USER_ID);
                        assertThat(role.getCreatedAt()).isEqualTo(RoleUseCaseTestSupport.NOW);
                    });
        }
    }
}
