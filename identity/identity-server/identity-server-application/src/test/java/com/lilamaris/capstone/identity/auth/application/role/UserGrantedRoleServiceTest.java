package com.lilamaris.capstone.identity.auth.application.role;

import com.lilamaris.capstone.identity.auth.application.account.port.out.UserReader;
import com.lilamaris.capstone.identity.auth.application.role.port.out.UserGrantedRoleReader;
import com.lilamaris.capstone.identity.auth.application.role.port.out.UserGrantedRoleStore;
import com.lilamaris.capstone.identity.auth.application.role.port.out.criteria.UserGrantRoleLookupCriteria;
import com.lilamaris.capstone.identity.auth.application.role.service.UserGrantedRoleService;
import com.lilamaris.capstone.identity.auth.application.shared.exception.IdentityAuthApplicationErrorCode;
import com.lilamaris.capstone.identity.auth.domain.role.UserGrantedRole;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
@DisplayName("UserGrantedRole 유스케이스 흐름 테스트")
class UserGrantedRoleServiceTest {
    @Mock
    UserGrantedRoleReader reader;

    @Mock
    UserGrantedRoleStore store;

    @Mock
    UserReader userReader;

    UserGrantedRoleService service;

    @BeforeEach
    void setUp() {
        service = new UserGrantedRoleService(
                reader,
                store,
                userReader,
                RoleUseCaseTestSupport.CLOCK
        );
    }

    @Nested
    @DisplayName("권한 부여")
    class GrantTest {
        @Test
        @DisplayName("권한 부여 권한이 있으면 사용자 권한을 저장한다")
        void save_user_granted_role_when_requester_has_grant_capability() {
            when(userReader.existsById(RoleUseCaseTestSupport.USER_ID)).thenReturn(true);
            when(reader.existsByCriteria(any(UserGrantRoleLookupCriteria.class))).thenReturn(false);

            var result = service.grant(RoleUseCaseTestSupport.grantRoleCommand());

            verify(userReader).existsById(RoleUseCaseTestSupport.USER_ID);
            verify(reader).existsByCriteria(any(UserGrantRoleLookupCriteria.class));
            verify(store).save(any(UserGrantedRole.class));
            assertThat(result.userId()).isEqualTo(RoleUseCaseTestSupport.USER_ID);
            assertThat(result.namespace()).isEqualTo(RoleUseCaseTestSupport.NAMESPACE_NAME);
            assertThat(result.role()).isEqualTo(RoleUseCaseTestSupport.ROLE);
            assertThat(result.createdAt()).isEqualTo(RoleUseCaseTestSupport.NOW);
        }

        @Test
        @DisplayName("권한 부여 권한이 없으면 예외")
        void throw_exception_when_requester_has_no_grant_capability() {
            RoleUseCaseTestSupport.assertApplicationError(
                    () -> service.grant(RoleUseCaseTestSupport.unauthorizedGrantRoleCommand()),
                    IdentityAuthApplicationErrorCode.UNAUTHORIZED_GRANT_ATTEMPT
            );
        }

        @Test
        @DisplayName("권한을 부여할 사용자가 없으면 예외")
        void throw_exception_when_user_not_found() {
            when(userReader.existsById(RoleUseCaseTestSupport.USER_ID)).thenReturn(false);

            RoleUseCaseTestSupport.assertApplicationError(
                    () -> service.grant(RoleUseCaseTestSupport.grantRoleCommand()),
                    IdentityAuthApplicationErrorCode.USER_NOT_FOUND
            );
        }

        @Test
        @DisplayName("이미 부여된 사용자 권한이면 예외")
        void throw_exception_when_user_granted_role_already_exists() {
            when(userReader.existsById(RoleUseCaseTestSupport.USER_ID)).thenReturn(true);
            when(reader.existsByCriteria(any(UserGrantRoleLookupCriteria.class))).thenReturn(true);

            RoleUseCaseTestSupport.assertApplicationError(
                    () -> service.grant(RoleUseCaseTestSupport.grantRoleCommand()),
                    IdentityAuthApplicationErrorCode.USER_GRANT_ALREADY_EXISTS
            );
        }
    }

    @Nested
    @DisplayName("권한 회수")
    class RevokeTest {
        @Test
        @DisplayName("권한 회수 권한이 있으면 사용자 권한을 삭제한다")
        void delete_user_granted_role_when_requester_has_revoke_capability() {
            when(userReader.existsById(RoleUseCaseTestSupport.USER_ID)).thenReturn(true);
            when(reader.findByCriteria(any(UserGrantRoleLookupCriteria.class)))
                    .thenReturn(Optional.of(RoleUseCaseTestSupport.userGrantedRole()));

            service.revoke(RoleUseCaseTestSupport.revokeRoleCommand());

            verify(userReader).existsById(RoleUseCaseTestSupport.USER_ID);
            verify(reader).findByCriteria(any(UserGrantRoleLookupCriteria.class));
            verify(store).delete(any(UserGrantedRole.class));
        }

        @Test
        @DisplayName("권한 회수 권한이 없으면 예외")
        void throw_exception_when_requester_has_no_revoke_capability() {
            RoleUseCaseTestSupport.assertApplicationError(
                    () -> service.revoke(RoleUseCaseTestSupport.unauthorizedRevokeRoleCommand()),
                    IdentityAuthApplicationErrorCode.UNAUTHORIZED_REVOKE_ATTEMPT
            );
        }

        @Test
        @DisplayName("권한을 회수할 사용자가 없으면 예외")
        void throw_exception_when_user_not_found() {
            when(userReader.existsById(RoleUseCaseTestSupport.USER_ID)).thenReturn(false);

            RoleUseCaseTestSupport.assertApplicationError(
                    () -> service.revoke(RoleUseCaseTestSupport.revokeRoleCommand()),
                    IdentityAuthApplicationErrorCode.USER_NOT_FOUND
            );
        }

        @Test
        @DisplayName("회수할 사용자 권한이 없으면 예외")
        void throw_exception_when_user_granted_role_not_found() {
            when(userReader.existsById(RoleUseCaseTestSupport.USER_ID)).thenReturn(true);
            when(reader.findByCriteria(any(UserGrantRoleLookupCriteria.class))).thenReturn(Optional.empty());

            RoleUseCaseTestSupport.assertApplicationError(
                    () -> service.revoke(RoleUseCaseTestSupport.revokeRoleCommand()),
                    IdentityAuthApplicationErrorCode.USER_GRANT_NOT_FOUND
            );
        }
    }

    @Nested
    @DisplayName("권한 목록 조회")
    class ListTest {
        @Test
        @DisplayName("사용자 권한 목록을 조회한다")
        void find_user_granted_roles() {
            when(userReader.existsById(RoleUseCaseTestSupport.USER_ID)).thenReturn(true);
            when(reader.findByUserId(RoleUseCaseTestSupport.USER_ID))
                    .thenReturn(List.of(RoleUseCaseTestSupport.userGrantedRole()));

            var results = service.list(RoleUseCaseTestSupport.listUserGrantedRoleQuery());

            verify(userReader).existsById(RoleUseCaseTestSupport.USER_ID);
            verify(reader).findByUserId(RoleUseCaseTestSupport.USER_ID);
            assertThat(results)
                    .hasSize(1)
                    .first()
                    .satisfies(result -> {
                        assertThat(result.userId()).isEqualTo(RoleUseCaseTestSupport.USER_ID);
                        assertThat(result.namespace()).isEqualTo(RoleUseCaseTestSupport.NAMESPACE_NAME);
                        assertThat(result.role()).isEqualTo(RoleUseCaseTestSupport.ROLE);
                        assertThat(result.createdAt()).isEqualTo(RoleUseCaseTestSupport.NOW);
                    });
        }

        @Test
        @DisplayName("목록을 조회할 사용자가 없으면 예외")
        void throw_exception_when_user_not_found() {
            when(userReader.existsById(RoleUseCaseTestSupport.USER_ID)).thenReturn(false);

            RoleUseCaseTestSupport.assertApplicationError(
                    () -> service.list(RoleUseCaseTestSupport.listUserGrantedRoleQuery()),
                    IdentityAuthApplicationErrorCode.USER_NOT_FOUND
            );
        }
    }
}
