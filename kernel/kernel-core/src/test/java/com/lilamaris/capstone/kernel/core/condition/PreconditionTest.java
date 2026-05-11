package com.lilamaris.capstone.kernel.core.condition;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Precondition 테스트")
class PreconditionTest {

    @Test
    @DisplayName("음수 Duraiton을 요구할 수 있다")
    void require_negative_duration() {
        var negative = Duration.ofSeconds(-1);

        assertThat(Preconditions.requireNegative(negative, "duration")).isEqualTo(negative);

        assertThatThrownBy(() -> Preconditions.requireNegative(Duration.ZERO, "duration"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("duration must be negative.");
        assertThatThrownBy(() -> Preconditions.requireNegative(Duration.ofSeconds(1), "duration"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("duration must be negative.");
    }

    @Test
    @DisplayName("0 이상의 Duration을 요구할 수 있다")
    void require_non_negative_duration() {
        assertThat(Preconditions.requireNonNegative(Duration.ZERO, "duration")).isEqualTo(Duration.ZERO);
        assertThat(Preconditions.requireNonNegative(Duration.ofSeconds(1), "duration")).isEqualTo(Duration.ofSeconds(1));

        assertThatThrownBy(() -> Preconditions.requireNonNegative(Duration.ofSeconds(-1), "duration"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("duration must be non-negative.");
    }

    @Test
    @DisplayName("양수 Duration을 요구할 수 있다")
    void require_positive_duration() {
        var positive = Duration.ofSeconds(1);

        assertThat(Preconditions.requirePositive(positive, "duration")).isEqualTo(positive);

        assertThatThrownBy(() -> Preconditions.requirePositive(Duration.ZERO, "duration"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("duration must be positive.");
        assertThatThrownBy(() -> Preconditions.requirePositive(Duration.ofSeconds(-1), "duration"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("duration must be positive.");
    }

    @Test
    @DisplayName("0 이하의 Duration을 요구할 수 있다")
    void require_non_positive_duration() {
        assertThat(Preconditions.requireNonPositive(Duration.ofSeconds(-1), "duration")).isEqualTo(Duration.ofSeconds(-1));
        assertThat(Preconditions.requireNonPositive(Duration.ZERO, "duration")).isEqualTo(Duration.ZERO);

        assertThatThrownBy(() -> Preconditions.requireNonPositive(Duration.ofSeconds(1), "duration"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("duration must be non-positive.");
    }

    @Test
    @DisplayName("Duration 부호 검증 대상이 null이면 예외를 던진다")
    void throw_when_signed_duration_is_null() {
        assertThatThrownBy(() -> Preconditions.requireNegative((Duration) null, "duration"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("duration must not be null.");
        assertThatThrownBy(() -> Preconditions.requireNonNegative((Duration) null, "duration"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("duration must not be null.");
        assertThatThrownBy(() -> Preconditions.requirePositive((Duration) null, "duration"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("duration must not be null.");
        assertThatThrownBy(() -> Preconditions.requireNonPositive((Duration) null, "duration"))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("duration must not be null.");
    }
}
