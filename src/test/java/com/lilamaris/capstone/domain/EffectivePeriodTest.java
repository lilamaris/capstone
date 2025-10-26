package com.lilamaris.capstone.domain;

import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EffectivePeriodTest {
    private LocalDateTime now;

    @BeforeEach
    void setup() {
        this.now = EffectivePeriod.now();
    }

    @Test
    void should_create() {
        var ef1 = EffectivePeriod.builder()
                .from(now)
                .build();
        assertThat(ef1.from()).isEqualTo(now);

        var ef2 = EffectivePeriod.builder()
                .to(now.plusDays(1))
                .build();

        assertThat(ef2.to()).isEqualTo(now.plusDays(1));
    }

    @Test
    void should_throw_when_create() {
        ThrowableAssert.ThrowingCallable thrown = () -> {
            EffectivePeriod.builder()
                    .from(now)
                    .to(now.minusDays(1))
                    .build();
        };

        assertThatThrownBy(thrown)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("'to' is must be after than 'from'");
    }

    void assertOverlap(EffectivePeriod a, EffectivePeriod b, Boolean expect) {
        assertThat(a.isOverlap(b)).isEqualTo(expect);
        assertThat(b.isOverlap(a)).isEqualTo(expect);
    }

    void assertOverlap(EffectivePeriod a, LocalDateTime time, Boolean expect) {
        assertThat(a.contains(time)).isEqualTo(expect);
    }

    @Test
    void should_detect_overlap_halfOpen() {
        var after3 = now.plusDays(3);
        var ef1 = EffectivePeriod.builder()
                .from(now)
                .to(after3)
                .build();

        var ef2 = ef1.toBuilder().from(now.plusDays(1)).build();
        assertOverlap(ef1, ef2, true);

        ef2 = ef1.toBuilder().from(now.minusDays(1)).build();
        assertOverlap(ef1, ef2, true);

        ef2 = ef1.toBuilder().to(after3.plusDays(1)).build();
        assertOverlap(ef1, ef2, true);

        ef2 = ef1.toBuilder().to(after3.minusDays(1)).build();
        assertOverlap(ef1, ef2, true);

        ef2 = ef1.toBuilder().from(now.plusDays(1)).to(after3.plusDays(1)).build();
        assertOverlap(ef1, ef2, true);

        ef2 = ef1.toBuilder().from(now.minusDays(1)).to(after3.minusDays(1)).build();
        assertOverlap(ef1, ef2, true);

        ef2 = ef1.toBuilder().from(now.plusDays(1)).to(after3.minusDays(1)).build();
        assertOverlap(ef1, ef2, true);

        ef2 = ef1.toBuilder().from(now.minusDays(1)).to(after3.plusDays(1)).build();
        assertOverlap(ef1, ef2, true);

        ef2 = ef1.toBuilder().from(now.minusDays(1)).to(now).build();
        assertOverlap(ef1, ef2, false);

        ef2 = ef1.toBuilder().from(after3).to(after3.plusDays(1)).build();
        assertOverlap(ef1, ef2, false);

        assertOverlap(ef1, now, false);
        assertOverlap(ef1, after3, false);
        assertOverlap(ef1, now.plusNanos(1), true);
        assertOverlap(ef1, after3.minusNanos(1), true);
    }
}
