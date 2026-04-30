package com.lilamaris.capstone.identity.auth.domain.account;

import com.lilamaris.capstone.identity.auth.domain.UserFixture;
import com.lilamaris.capstone.kernel.testsupport.FixedClock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Clock;
import java.time.Instant;

import static com.lilamaris.capstone.kernel.testsupport.assertion.DomainAssertions.assertThatDomainThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("FederatedAccount 테스트")
public class FederatedAccountTest {
    Clock clock = FixedClock.getFixed();

    Instant now = clock.instant();

    User user = UserFixture.createUser(now);

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {
        @Test
        @DisplayName("user, registrationId, providerUserId, createdAt으로 생성한다")
        void create_with_user_registration_id_provider_user_id_and_created_at() {
            var registrationId = "google";
            var providerUserId = "google-user-1";

            var account = FederatedAccount.of(user, registrationId, providerUserId, now);

            assertThat(account.getUser()).isSameAs(user);
            assertThat(account.getRegistrationId()).isEqualTo(registrationId);
            assertThat(account.getProviderUserId()).isEqualTo(providerUserId);
            assertThat(account.getCreatedAt()).isEqualTo(now);
        }

        @ParameterizedTest(name = "user = {0}")
        @NullSource
        @DisplayName("user가 null이면 예외")
        void throw_exception_when_null_user(User user) {
            assertThatDomainThrownBy(() -> FederatedAccount.of(user, "google", "google-user-1", now))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("user");
        }

        @ParameterizedTest(name = "registrationId = {0}")
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("registrationId가 빈 문자열이면 예외")
        void throw_exception_when_blank_registration_id(String registrationId) {
            assertThatDomainThrownBy(() -> FederatedAccount.of(user, registrationId, "google-user-1", now))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasNonBlankMessageFor("registrationId");
        }

        @ParameterizedTest(name = "registrationId = {0}")
        @NullSource
        @DisplayName("registrationId가 null이면 예외")
        void throw_exception_when_null_registration_id(String registrationId) {
            assertThatDomainThrownBy(() -> FederatedAccount.of(user, registrationId, "google-user-1", now))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("registrationId");
        }

        @ParameterizedTest(name = "providerUserId = {0}")
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("providerUserId가 빈 문자열이면 예외")
        void throw_exception_when_blank_provider_user_id(String providerUserId) {
            assertThatDomainThrownBy(() -> FederatedAccount.of(user, "google", providerUserId, now))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasNonBlankMessageFor("providerUserId");
        }

        @ParameterizedTest(name = "providerUserId = {0}")
        @NullSource
        @DisplayName("providerUserId가 null이면 예외")
        void throw_exception_when_null_provider_user_id(String providerUserId) {
            assertThatDomainThrownBy(() -> FederatedAccount.of(user, "google", providerUserId, now))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("providerUserId");
        }

        @ParameterizedTest(name = "createdAt = {0}")
        @NullSource
        @DisplayName("createdAt이 null이면 예외")
        void throw_exception_when_null_created_at(Instant createdAt) {
            assertThatDomainThrownBy(() -> FederatedAccount.of(user, "google", "google-user-1", createdAt))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("createdAt");
        }
    }
}