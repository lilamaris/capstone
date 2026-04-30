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

@DisplayName("User 테스트")
public class UserTest {
    Clock clock = FixedClock.getFixed();

    Instant now = clock.instant();

    @Nested
    @DisplayName("생성 테스트")
    class CreationTest {
        @Test
        @DisplayName("nickname과 createdAt으로 생성한다")
        void create_with_nickname_and_created_at() {
            var nickname = "tester";

            var user = User.of(nickname, now);

            assertThat(user.getNickname()).isEqualTo(nickname);
            assertThat(user.getCreatedAt()).isEqualTo(now);
        }

        @ParameterizedTest(name = "nickname = {0}")
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("nickname이 빈 문자열이면 예외")
        void throw_exception_when_blank_nickname(String nickname) {
            assertThatDomainThrownBy(() -> User.of(nickname, now))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasNonBlankMessageFor("nickname");
        }

        @ParameterizedTest(name = "nickname = {0}")
        @NullSource
        @DisplayName("nickname이 null이면 예외")
        void throw_exception_when_null_nickname(String nickname) {
            assertThatDomainThrownBy(() -> User.of(nickname, now))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("nickname");
        }

        @ParameterizedTest(name = "createdAt = {0}")
        @NullSource
        @DisplayName("createdAt이 null이면 예외")
        void throw_exception_when_null_created_at(Instant createdAt) {
            assertThatDomainThrownBy(() -> User.of("tester", createdAt))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("createdAt");
        }
    }

    @Nested
    @DisplayName("닉네임 변경 테스트")
    class UpdateNicknameTest {
        @Test
        @DisplayName("nickname을 변경한다")
        void update_nickname() {
            var user = UserFixture.createUser(now);

            user.updateNickname("updated");

            assertThat(user.getNickname()).isEqualTo("updated");
        }

        @ParameterizedTest(name = "nickname = {0}")
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("변경할 nickname이 빈 문자열이면 예외")
        void throw_exception_when_update_nickname_is_blank(String nickname) {
            var user = UserFixture.createUser(now);

            assertThatDomainThrownBy(() -> user.updateNickname(nickname))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasNonBlankMessageFor("nickname");
        }

        @ParameterizedTest(name = "nickname = {0}")
        @NullSource
        @DisplayName("변경할 nickname이 null이면 예외")
        void throw_exception_when_update_nickname_is_null(String nickname) {
            var user = UserFixture.createUser(now);

            assertThatDomainThrownBy(() -> user.updateNickname(nickname))
                    .isInstanceOf(NullPointerException.class)
                    .hasNonNullMessageFor("nickname");
        }
    }
}
