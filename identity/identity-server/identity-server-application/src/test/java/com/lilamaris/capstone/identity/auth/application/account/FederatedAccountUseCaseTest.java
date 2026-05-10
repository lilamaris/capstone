package com.lilamaris.capstone.identity.auth.application.account;

import com.lilamaris.capstone.identity.auth.application.account.model.UserAccountProvisioner;
import com.lilamaris.capstone.identity.auth.application.account.port.out.FederatedAccountReader;
import com.lilamaris.capstone.identity.auth.application.account.port.out.FederatedAccountStore;
import com.lilamaris.capstone.identity.auth.application.account.port.out.UserReader;
import com.lilamaris.capstone.identity.auth.application.account.port.out.UserStore;
import com.lilamaris.capstone.identity.auth.application.account.port.out.criteria.FederatedProviderLookupCriteria;
import com.lilamaris.capstone.identity.auth.application.account.port.out.criteria.FederatedUserLookupCriteria;
import com.lilamaris.capstone.identity.auth.application.account.service.FederatedAccountService;
import com.lilamaris.capstone.identity.auth.application.role.internal.InitialUserGrantedRoleProvisioner;
import com.lilamaris.capstone.identity.auth.application.role.port.out.UserGrantedRoleReader;
import com.lilamaris.capstone.identity.auth.application.shared.exception.IdentityAuthApplicationErrorCode;
import com.lilamaris.capstone.identity.auth.domain.account.FederatedAccount;
import com.lilamaris.capstone.identity.auth.domain.account.User;
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
@DisplayName("FederatedAccount 유스케이스 흐름 테스트")
class FederatedAccountUseCaseTest {
    @Mock
    FederatedAccountReader reader;

    @Mock
    FederatedAccountStore store;

    @Mock
    UserReader userReader;

    @Mock
    UserStore userStore;

    @Mock
    UserGrantedRoleReader userGrantedRoleReader;

    @Mock
    InitialUserGrantedRoleProvisioner roleProvisioner;

    UserAccountProvisioner provisioner = new UserAccountProvisioner();

    FederatedAccountService service;

    @BeforeEach
    void setUp() {
        service = new FederatedAccountService(
                reader,
                store,
                userReader,
                userStore,
                userGrantedRoleReader,
                provisioner,
                roleProvisioner,
                AccountUseCaseTestSupport.CLOCK
        );
    }

    @Nested
    @DisplayName("federated 인증")
    class AuthenticateTest {
        @Test
        @DisplayName("사용자와 federated 계정을 저장한다")
        void save_user_and_federated_account() {
            when(userStore.save(any(User.class))).thenReturn(AccountUseCaseTestSupport.savedUser());
            when(roleProvisioner.grant(AccountUseCaseTestSupport.USER_ID))
                    .thenReturn(List.of(AccountUseCaseTestSupport.userGrantedRole()));

            var result = service.authenticate(AccountUseCaseTestSupport.authenticateFederatedAccountCommand());

            verify(reader).existsByCriteria(any(FederatedProviderLookupCriteria.class));
            verify(userStore).save(any(User.class));
            verify(store).save(any(FederatedAccount.class));
            verify(roleProvisioner).grant(AccountUseCaseTestSupport.USER_ID);
            assertThat(result.grantedRoles())
                    .extracting(role -> role.namespace().name())
                    .containsExactly(AccountUseCaseTestSupport.NAMESPACE_NAME);
        }

        @Test
        @DisplayName("이미 연결된 provider 계정이면 예외")
        void throw_exception_when_provider_account_already_exists() {
            when(reader.existsByCriteria(any(FederatedProviderLookupCriteria.class))).thenReturn(true);

            AccountUseCaseTestSupport.assertApplicationError(
                    () -> service.authenticate(AccountUseCaseTestSupport.authenticateFederatedAccountCommand()),
                    IdentityAuthApplicationErrorCode.ACCOUNT_ALREADY_EXISTS
            );
        }
    }

    @Nested
    @DisplayName("federated 계정 연결")
    class LinkTest {
        @Test
        @DisplayName("사용자를 조회하고 federated 계정을 저장한다")
        void find_user_and_save_federated_account() {
            when(userReader.findById(AccountUseCaseTestSupport.USER_ID))
                    .thenReturn(Optional.of(AccountUseCaseTestSupport.user()));
            when(userGrantedRoleReader.findByUserId(AccountUseCaseTestSupport.USER_ID))
                    .thenReturn(List.of(AccountUseCaseTestSupport.userGrantedRole()));

            var result = service.link(AccountUseCaseTestSupport.linkFederatedAccountCommand());

            verify(reader).existsByCriteria(any(FederatedProviderLookupCriteria.class));
            verify(userReader).findById(AccountUseCaseTestSupport.USER_ID);
            verify(store).save(any(FederatedAccount.class));
            verify(userGrantedRoleReader).findByUserId(AccountUseCaseTestSupport.USER_ID);
            assertThat(result.grantedRoles())
                    .extracting(role -> role.namespace().name())
                    .containsExactly(AccountUseCaseTestSupport.NAMESPACE_NAME);
        }

        @Test
        @DisplayName("이미 연결된 provider 계정이면 예외")
        void throw_exception_when_provider_account_already_exists() {
            when(reader.existsByCriteria(any(FederatedProviderLookupCriteria.class))).thenReturn(true);

            AccountUseCaseTestSupport.assertApplicationError(
                    () -> service.link(AccountUseCaseTestSupport.linkFederatedAccountCommand()),
                    IdentityAuthApplicationErrorCode.ACCOUNT_ALREADY_EXISTS
            );
        }

        @Test
        @DisplayName("연결할 사용자가 없으면 예외")
        void throw_exception_when_user_not_found() {
            when(reader.existsByCriteria(any(FederatedProviderLookupCriteria.class))).thenReturn(false);
            when(userReader.findById(AccountUseCaseTestSupport.USER_ID)).thenReturn(Optional.empty());

            AccountUseCaseTestSupport.assertApplicationError(
                    () -> service.link(AccountUseCaseTestSupport.linkFederatedAccountCommand()),
                    IdentityAuthApplicationErrorCode.USER_NOT_FOUND
            );
        }
    }

    @Nested
    @DisplayName("federated 계정 연결 해제")
    class UnlinkTest {
        @Test
        @DisplayName("조회 조건으로 federated 계정을 찾아 삭제한다")
        void find_account_by_criteria_and_delete() {
            when(reader.findByCriteria(any(FederatedUserLookupCriteria.class)))
                    .thenReturn(Optional.of(AccountUseCaseTestSupport.federatedAccount()));

            service.unlink(AccountUseCaseTestSupport.unlinkFederatedAccountCommand());

            verify(reader).findByCriteria(any(FederatedUserLookupCriteria.class));
            verify(store).delete(any(FederatedAccount.class));
        }

        @Test
        @DisplayName("해제할 federated 계정이 없으면 예외")
        void throw_exception_when_account_not_found() {
            when(reader.findByCriteria(any(FederatedUserLookupCriteria.class))).thenReturn(Optional.empty());

            AccountUseCaseTestSupport.assertApplicationError(
                    () -> service.unlink(AccountUseCaseTestSupport.unlinkFederatedAccountCommand()),
                    IdentityAuthApplicationErrorCode.ACCOUNT_NOT_FOUND
            );
        }
    }

    @Nested
    @DisplayName("federated 계정 목록 조회")
    class ListTest {
        @Test
        @DisplayName("사용자 존재를 확인하고 사용자 federated 계정 목록을 조회한다")
        void check_user_and_find_accounts() {
            when(userReader.existsById(AccountUseCaseTestSupport.USER_ID)).thenReturn(true);
            when(reader.findByUserId(AccountUseCaseTestSupport.USER_ID))
                    .thenReturn(List.of(AccountUseCaseTestSupport.federatedAccount()));

            service.list(AccountUseCaseTestSupport.listFederatedAccountCommand());

            verify(userReader).existsById(AccountUseCaseTestSupport.USER_ID);
            verify(reader).findByUserId(AccountUseCaseTestSupport.USER_ID);
        }

        @Test
        @DisplayName("목록을 조회할 사용자가 없으면 예외")
        void throw_exception_when_user_not_found() {
            when(userReader.existsById(AccountUseCaseTestSupport.USER_ID)).thenReturn(false);

            AccountUseCaseTestSupport.assertApplicationError(
                    () -> service.list(AccountUseCaseTestSupport.listFederatedAccountCommand()),
                    IdentityAuthApplicationErrorCode.USER_NOT_FOUND
            );
        }
    }
}
