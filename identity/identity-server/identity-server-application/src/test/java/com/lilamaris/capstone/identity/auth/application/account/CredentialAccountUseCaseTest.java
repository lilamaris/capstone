package com.lilamaris.capstone.identity.auth.application.account;

import com.lilamaris.capstone.identity.auth.application.account.model.UserAccountProvisioner;
import com.lilamaris.capstone.identity.auth.application.account.port.out.CredentialAccountReader;
import com.lilamaris.capstone.identity.auth.application.account.port.out.CredentialAccountStore;
import com.lilamaris.capstone.identity.auth.application.account.port.out.UserStore;
import com.lilamaris.capstone.identity.auth.application.account.service.CredentialAccountService;
import com.lilamaris.capstone.identity.auth.application.role.internal.InitialUserGrantedRoleProvisioner;
import com.lilamaris.capstone.identity.auth.application.role.port.out.UserGrantedRoleReader;
import com.lilamaris.capstone.identity.auth.application.shared.exception.IdentityAuthApplicationErrorCode;
import com.lilamaris.capstone.identity.auth.domain.account.CredentialAccount;
import com.lilamaris.capstone.identity.auth.domain.account.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
@DisplayName("CredentialAccount 유스케이스 흐름 테스트")
class CredentialAccountUseCaseTest {
    @Mock
    CredentialAccountReader reader;

    @Mock
    CredentialAccountStore store;

    @Mock
    UserStore userStore;

    @Mock
    UserGrantedRoleReader userGrantedRoleReader;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    InitialUserGrantedRoleProvisioner roleProvisioner;

    UserAccountProvisioner provisioner = new UserAccountProvisioner();

    CredentialAccountService service;

    @BeforeEach
    void setUp() {
        service = new CredentialAccountService(
                reader,
                store,
                userStore,
                userGrantedRoleReader,
                provisioner,
                roleProvisioner,
                passwordEncoder,
                AccountUseCaseTestSupport.CLOCK
        );
    }

    @Nested
    @DisplayName("회원가입")
    class RegisterTest {
        @Test
        @DisplayName("사용자와 credential 계정을 저장한다")
        void save_user_and_credential_account() {
            when(reader.existsByEmail(AccountUseCaseTestSupport.EMAIL)).thenReturn(false);
            when(passwordEncoder.encode(AccountUseCaseTestSupport.RAW_PASSWORD))
                    .thenReturn(AccountUseCaseTestSupport.PASSWORD_HASH);
            when(userStore.save(any(User.class))).thenReturn(AccountUseCaseTestSupport.savedUser());
            when(roleProvisioner.grant(AccountUseCaseTestSupport.USER_ID))
                    .thenReturn(List.of(AccountUseCaseTestSupport.userGrantedRole()));

            var result = service.register(AccountUseCaseTestSupport.registerCredentialAccountCommand());

            verify(userStore).save(any(User.class));
            verify(store).save(any(CredentialAccount.class));
            verify(roleProvisioner).grant(AccountUseCaseTestSupport.USER_ID);
            assertThat(result.grantedRoles())
                    .extracting(role -> role.namespace().name())
                    .containsExactly(AccountUseCaseTestSupport.NAMESPACE_NAME);
        }

        @Test
        @DisplayName("이미 등록된 이메일이면 예외")
        void throw_exception_when_email_is_duplicated() {
            when(reader.existsByEmail(AccountUseCaseTestSupport.EMAIL)).thenReturn(true);

            AccountUseCaseTestSupport.assertApplicationError(
                    () -> service.register(AccountUseCaseTestSupport.registerCredentialAccountCommand()),
                    IdentityAuthApplicationErrorCode.CREDENTIAL_EMAIL_DUPLICATED
            );
        }
    }

    @Nested
    @DisplayName("credential 인증")
    class AuthenticateTest {
        @Test
        @DisplayName("이메일로 credential 계정을 조회하고 비밀번호를 확인한다")
        void find_account_and_check_password() {
            var account = AccountUseCaseTestSupport.credentialAccount();
            when(reader.findByEmail(AccountUseCaseTestSupport.EMAIL)).thenReturn(Optional.of(account));
            when(passwordEncoder.matches(AccountUseCaseTestSupport.RAW_PASSWORD, account.getPasswordHash()))
                    .thenReturn(true);
            when(userGrantedRoleReader.findByUserId(account.getUser().getId()))
                    .thenReturn(List.of(AccountUseCaseTestSupport.userGrantedRole()));

            var result = service.authenticate(AccountUseCaseTestSupport.authenticateCredentialAccountCommand());

            verify(reader).findByEmail(AccountUseCaseTestSupport.EMAIL);
            verify(passwordEncoder).matches(AccountUseCaseTestSupport.RAW_PASSWORD, account.getPasswordHash());
            verify(userGrantedRoleReader).findByUserId(account.getUser().getId());
            assertThat(result.grantedRoles())
                    .extracting(role -> role.namespace().name())
                    .containsExactly(AccountUseCaseTestSupport.NAMESPACE_NAME);
        }

        @Test
        @DisplayName("이메일에 해당하는 credential 계정이 없으면 인증 실패 예외")
        void throw_exception_when_account_not_found() {
            when(reader.findByEmail(AccountUseCaseTestSupport.EMAIL)).thenReturn(Optional.empty());

            AccountUseCaseTestSupport.assertApplicationError(
                    () -> service.authenticate(AccountUseCaseTestSupport.authenticateCredentialAccountCommand()),
                    IdentityAuthApplicationErrorCode.AUTHENTICATION_FAILED
            );
        }

        @Test
        @DisplayName("비밀번호가 일치하지 않으면 인증 실패 예외")
        void throw_exception_when_password_does_not_match() {
            var account = AccountUseCaseTestSupport.credentialAccount();
            when(reader.findByEmail(AccountUseCaseTestSupport.EMAIL)).thenReturn(Optional.of(account));
            when(passwordEncoder.matches(AccountUseCaseTestSupport.RAW_PASSWORD, account.getPasswordHash()))
                    .thenReturn(false);

            AccountUseCaseTestSupport.assertApplicationError(
                    () -> service.authenticate(AccountUseCaseTestSupport.authenticateCredentialAccountCommand()),
                    IdentityAuthApplicationErrorCode.AUTHENTICATION_FAILED
            );
        }
    }

    @Nested
    @DisplayName("credential 비밀번호 변경")
    class ChangePasswordTest {
        @Test
        @DisplayName("사용자 credential 계정의 비밀번호 해시를 변경해 저장한다")
        void update_password_hash_and_save_account() {
            var account = AccountUseCaseTestSupport.credentialAccount();
            when(reader.findByUserId(AccountUseCaseTestSupport.USER_ID)).thenReturn(Optional.of(account));
            when(passwordEncoder.matches(AccountUseCaseTestSupport.RAW_PASSWORD, account.getPasswordHash()))
                    .thenReturn(true);
            when(passwordEncoder.encode(AccountUseCaseTestSupport.NEW_RAW_PASSWORD))
                    .thenReturn(AccountUseCaseTestSupport.NEW_PASSWORD_HASH);

            service.change(AccountUseCaseTestSupport.changeCredentialAccountCommand());

            verify(store).save(any(CredentialAccount.class));
        }

        @Test
        @DisplayName("사용자의 credential 계정이 없으면 예외")
        void throw_exception_when_account_not_found() {
            when(reader.findByUserId(AccountUseCaseTestSupport.USER_ID)).thenReturn(Optional.empty());

            AccountUseCaseTestSupport.assertApplicationError(
                    () -> service.change(AccountUseCaseTestSupport.changeCredentialAccountCommand()),
                    IdentityAuthApplicationErrorCode.ACCOUNT_NOT_FOUND
            );
        }

        @Test
        @DisplayName("기존 비밀번호가 일치하지 않으면 인증 실패 예외")
        void throw_exception_when_old_password_does_not_match() {
            var account = AccountUseCaseTestSupport.credentialAccount();
            when(reader.findByUserId(AccountUseCaseTestSupport.USER_ID)).thenReturn(Optional.of(account));
            when(passwordEncoder.matches(AccountUseCaseTestSupport.RAW_PASSWORD, account.getPasswordHash()))
                    .thenReturn(false);

            AccountUseCaseTestSupport.assertApplicationError(
                    () -> service.change(AccountUseCaseTestSupport.changeCredentialAccountCommand()),
                    IdentityAuthApplicationErrorCode.AUTHENTICATION_FAILED
            );
        }
    }
}
