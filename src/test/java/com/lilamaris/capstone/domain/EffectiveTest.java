package com.lilamaris.capstone.domain;

import com.lilamaris.capstone.application.util.UniversityClock;
import com.lilamaris.capstone.domain.embed.Effective;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EffectiveTest {
    private Instant now;

    @BeforeEach
    void setup() {
        this.now = UniversityClock.now();
    }

    @Test
    void should_create() {
        var ef1 = Effective.builder()
                .from(now)
                .build();
        assertThat(ef1.from()).isEqualTo(now);

        var ef2 = Effective.builder()
                .to(now.plus(1, ChronoUnit.DAYS))
                .build();

        assertThat(ef2.to()).isEqualTo(now.plus(1, ChronoUnit.DAYS));
    }

    @Test
    void should_throw_when_create() {
        ThrowableAssert.ThrowingCallable thrown = () -> Effective.builder()
                .from(now)
                .to(now.minus(1, ChronoUnit.DAYS))
                .build();

        assertThatThrownBy(thrown)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("'to' is must be after than 'from'");
    }

    void assertOverlap(Effective a, Effective b, Boolean expect) {
        assertThat(a.isOverlap(b)).isEqualTo(expect);
        assertThat(b.isOverlap(a)).isEqualTo(expect);
    }

    void assertOverlap(Effective a, Instant time, Boolean expect) {
        assertThat(a.contains(time)).isEqualTo(expect);
    }

    @Test
    void should_detect_overlap_halfOpen() {
        var after3 = now.plus(3, ChronoUnit.DAYS);
        var ef1 = Effective.builder()
                .from(now)
                .to(after3)
                .build();

        var ef2 = ef1.toBuilder().from(now.plus(1, ChronoUnit.DAYS)).build();
        assertOverlap(ef1, ef2, true);

        ef2 = ef1.toBuilder().from(now.minus(1, ChronoUnit.DAYS)).build();
        assertOverlap(ef1, ef2, true);

        ef2 = ef1.toBuilder().to(after3.plus(1, ChronoUnit.DAYS)).build();
        assertOverlap(ef1, ef2, true);

        ef2 = ef1.toBuilder().to(after3.minus(1, ChronoUnit.DAYS)).build();
        assertOverlap(ef1, ef2, true);

        ef2 = ef1.toBuilder().from(now.plus(1, ChronoUnit.DAYS)).to(after3.plus(1, ChronoUnit.DAYS)).build();
        assertOverlap(ef1, ef2, true);

        ef2 = ef1.toBuilder().from(now.minus(1, ChronoUnit.DAYS)).to(after3.minus(1, ChronoUnit.DAYS)).build();
        assertOverlap(ef1, ef2, true);

        ef2 = ef1.toBuilder().from(now.plus(1, ChronoUnit.DAYS)).to(after3.minus(1, ChronoUnit.DAYS)).build();
        assertOverlap(ef1, ef2, true);

        ef2 = ef1.toBuilder().from(now.minus(1, ChronoUnit.DAYS)).to(after3.plus(1, ChronoUnit.DAYS)).build();
        assertOverlap(ef1, ef2, true);

        ef2 = ef1.toBuilder().from(now.minus(1, ChronoUnit.DAYS)).to(now).build();
        assertOverlap(ef1, ef2, false);

        ef2 = ef1.toBuilder().from(after3).to(after3.plus(1, ChronoUnit.DAYS)).build();
        assertOverlap(ef1, ef2, false);

        assertOverlap(ef1, now, false);
        assertOverlap(ef1, after3, false);
        assertOverlap(ef1, now.plusNanos(1), true);
        assertOverlap(ef1, after3.minusNanos(1), true);
    }

    @Test
    void should_detect_closed() {
        var ef1 = Effective.openAt(now);
        assertThat(ef1.to()).isEqualTo(Effective.MAX);
        assertThat(ef1.isOpen()).isEqualTo(true);

        var ef2 = ef1.closeAt(now.plus(1, ChronoUnit.DAYS));
        assertThat(ef2.isOpen()).isEqualTo(false);
    }
}
