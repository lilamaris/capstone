package com.lilamaris.capstone.identity.auth.application.account.model;

import com.lilamaris.capstone.kernel.testsupport.FixedClock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Clock;

import static com.lilamaris.capstone.identity.auth.application.account.AccountUseCaseTestSupport.*;
import static com.lilamaris.capstone.kernel.testsupport.assertion.DomainAssertions.assertThatDomainThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("unit")
@DisplayName("UserAccountProvisioner 테스트")
public class UserAccountProvisionerTest {
    Clock clock = FixedClock.getFixed();

    UserAccountProvisioner provisioner = new UserAccountProvisioner();

    @Nested
    @DisplayName("credential 사용자 생성 테스트")
    class CreateCredentialUserTest {
        @Test
        @DisplayName("사용자와 credential 계정을 함께 생성한다")
        void create_user_and_credential_account() {
            var now = clock.instant();

            var provisioned = provisioner.createCredentialUser(
                    NICKNAME,
                    EMAIL,
                    PASSWORD_HASH,
                    now
            );

            assertThat(provisioned.user().getNickname()).isEqualTo(NICKNAME);
            assertThat(provisioned.user().getCreatedAt()).isEqualTo(now);
            assertThat(provisioned.account().getUser()).isSameAs(provisioned.user());
            assertThat(provisioned.account().getEmail()).isEqualTo(EMAIL);
            assertThat(provisioned.account().getPasswordHash()).isEqualTo(PASSWORD_HASH);
            assertThat(provisioned.account().getCreatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("federated 사용자 생성 테스트")
    class CreateFederatedUserTest {
        @Test
        @DisplayName("사용자와 federated 계정을 함께 생성한다")
        void create_user_and_federated_account() {
            var now = clock.instant();

            var provisioned = provisioner.createFederatedUser(
                    NICKNAME,
                    REGISTRATION_ID,
                    PROVIDER_USER_ID,
                    now
            );

            assertThat(provisioned.user().getNickname()).isEqualTo(NICKNAME);
            assertThat(provisioned.user().getCreatedAt()).isEqualTo(now);
            assertThat(provisioned.account().getUser()).isSameAs(provisioned.user());
            assertThat(provisioned.account().getRegistrationId()).isEqualTo(REGISTRATION_ID);
            assertThat(provisioned.account().getProviderUserId()).isEqualTo(PROVIDER_USER_ID);
            assertThat(provisioned.account().getCreatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("federated 계정 연결 테스트")
    class LinkFederatedTest {
        @Test
        @DisplayName("기존 사용자에 federated 계정을 연결한다")
        void link_federated_account_to_existing_user() {
            var now = clock.instant();
            var user = user();

            var provisioned = provisioner.linkFederated(
                    user,
                    REGISTRATION_ID,
                    PROVIDER_USER_ID,
                    now
            );

            assertThat(provisioned.user()).isSameAs(user);
            assertThat(provisioned.account().getUser()).isSameAs(user);
            assertThat(provisioned.account().getRegistrationId()).isEqualTo(REGISTRATION_ID);
            assertThat(provisioned.account().getProviderUserId()).isEqualTo(PROVIDER_USER_ID);
            assertThat(provisioned.account().getCreatedAt()).isEqualTo(now);
        }
    }

    @Nested
    @DisplayName("사용자 생성 검증 테스트")
    class UserCreationValidationTest {
        @ParameterizedTest(name = "nickname = {0}")
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("nickname이 빈 문자열이면 예외")
        void throw_exception_when_nickname_is_blank(String nickname) {
            assertThatDomainThrownBy(() -> provisioner.createCredentialUser(
                    nickname,
                    EMAIL,
                    PASSWORD_HASH,
                    clock.instant()
            ))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasNonBlankMessageFor("nickname");
        }

        @ParameterizedTest(name = "nickname = {0}")
        @NullSource
        @DisplayName("nickname이 null이면 예외")
        void throw_exception_when_nickname_is_null(String nickname) {
            assertThatDomainThrownBy(() -> provisioner.createCredentialUser(
                    nickname,
                    EMAIL,
                    PASSWORD_HASH,
                    clock.instant()
            ))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("nickname");
        }

        @ParameterizedTest(name = "createdAt = {0}")
        @NullSource
        @DisplayName("createdAt이 null이면 예외")
        void throw_exception_when_created_at_is_null(java.time.Instant createdAt) {
            assertThatDomainThrownBy(() -> provisioner.createCredentialUser(
                    NICKNAME,
                    EMAIL,
                    PASSWORD_HASH,
                    createdAt
            ))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("createdAt");
        }
    }

}
