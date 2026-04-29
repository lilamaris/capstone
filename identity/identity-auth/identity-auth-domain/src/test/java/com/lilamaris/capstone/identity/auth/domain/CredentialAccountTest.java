package com.lilamaris.capstone.identity.auth.domain;

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

@DisplayName("CredentialAccount 테스트")
public class CredentialAccountTest {
    Clock clock = FixedClock.getFixed();

    Instant now = clock.instant();

    User user = UserFixture.createUser(now);

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {
        @Test
        @DisplayName("user, email, passwordHash로 생성한다")
        void create_with_user_email_and_password_hash() {
            var user = UserFixture.createUser(now);
            var email = "tester@example.com";
            var passwordHash = "{bcrypt}password-hash";

            var account = CredentialAccount.of(user, email, passwordHash, now);

            assertThat(account.getUser()).isSameAs(user);
            assertThat(account.getEmail()).isEqualTo(email);
            assertThat(account.getPasswordHash()).isEqualTo(passwordHash);
            assertThat(account.getCreatedAt()).isEqualTo(now);
        }

        @ParameterizedTest(name = "user = {0}")
        @NullSource
        @DisplayName("user가 null이면 예외")
        void throw_exception_when_null_user(User user) {
            assertThatDomainThrownBy(() -> CredentialAccount.of(user, "tester@example.com", "{bcrypt}hash", now))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("user");
        }

        @ParameterizedTest(name = "email = {0}")
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("email이 빈 문자열이면 예외")
        void throw_exception_when_blank_email(String email) {
            assertThatDomainThrownBy(() -> CredentialAccount.of(user, email, "{bcrypt}hash", now))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasNonBlankMessageFor("email");
        }

        @ParameterizedTest(name = "email = {0}")
        @NullSource
        @DisplayName("email이 null이면 예외")
        void throw_exception_when_null_email(String email) {
            assertThatDomainThrownBy(() -> CredentialAccount.of(user, email, "{bcrypt}hash", now))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("email");
        }

        @ParameterizedTest(name = "passwordHash = {0}")
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("passwordHash가 빈 문자열이면 예외")
        void throw_exception_when_blank_password_hash(String passwordHash) {
            assertThatDomainThrownBy(() -> CredentialAccount.of(user, "tester@example.com", passwordHash, now))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasNonBlankMessageFor("passwordHash");
        }

        @ParameterizedTest(name = "passwordHash = {0}")
        @NullSource
        @DisplayName("passwordHash가 null이면 예외")
        void throw_exception_when_null_password_hash(String passwordHash) {
            assertThatDomainThrownBy(() -> CredentialAccount.of(user, "tester@example.com", passwordHash, now))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("passwordHash");
        }
    }

    @Nested
    @DisplayName("비밀번호 해시 변경 테스트")
    class UpdatePasswordHashTest {
        @Test
        @DisplayName("passwordHash를 변경한다")
        void update_password_hash() {
            var account = CredentialAccountFixture.createCredentialAccount(now);
            var updatedPasswordHash = "{bcrypt}updated-password-hash";

            account.updatePasswordHash(updatedPasswordHash);

            assertThat(account.getPasswordHash()).isEqualTo(updatedPasswordHash);
        }

        @ParameterizedTest(name = "passwordHash = {0}")
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("변경할 passwordHash가 빈 문자열이면 예외")
        void throw_exception_when_update_password_hash_is_blank(String passwordHash) {
            var account = CredentialAccountFixture.createCredentialAccount(now);

            assertThatDomainThrownBy(() -> account.updatePasswordHash(passwordHash))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasNonBlankMessageFor("passwordHash");
        }

        @ParameterizedTest(name = "passwordHash = {0}")
        @NullSource
        @DisplayName("변경할 passwordHash가 null이면 예외")
        void throw_exception_when_update_password_hash_is_null(String passwordHash) {
            var account = CredentialAccountFixture.createCredentialAccount(now);

            assertThatDomainThrownBy(() -> account.updatePasswordHash(passwordHash))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("passwordHash");
        }
    }
}