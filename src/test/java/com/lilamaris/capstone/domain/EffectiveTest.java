package com.lilamaris.capstone.domain;

import com.lilamaris.capstone.shared.domain.exception.DomainIllegalArgumentException;
import com.lilamaris.capstone.shared.domain.metadata.EffectiveMetadata;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaEffectiveMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.AssertionsForClassTypes.*;

public class EffectiveTest {
    private Instant i1;
    private Instant i2;
    private Instant i3;
    private EffectiveMetadata e1;
    private EffectiveMetadata e2;
    private EffectiveMetadata e3;

    @BeforeEach
    void run() {
        i1 = Instant.parse("2000-01-01T00:00:00Z");
        i2 = Instant.parse("2000-01-10T00:00:00Z");
        i3 = Instant.parse("2000-01-15T00:00:00Z");
        e1 = JpaEffectiveMetadata.create(i1);
        e2 = JpaEffectiveMetadata.create(i1, i2);
        e3 = JpaEffectiveMetadata.create(i2, i3);
    }

    @Test
    void invariant() {
        assertThatThrownBy(
                () -> JpaEffectiveMetadata.create(i3, i2)
        ).isInstanceOf(DomainIllegalArgumentException.class);
        assertThat(e1.from()).isEqualTo(i1);
        assertThat(e1.to()).isEqualTo(EffectiveMetadata.MAX);
    }

    @Test
    void overlap_detect() {
        assertThat(e1.isOverlap(e2)).isTrue();
        assertThat(e1.isOverlap(e3)).isTrue();

        assertThat(e2.isOverlap(e3)).isFalse();
        assertThat(e3.isOverlap(e2)).isFalse();

        assertThat(e2.contains(i1)).isTrue();
        assertThat(e2.contains(i2)).isFalse();
        assertThat(e2.contains(i3)).isFalse();
    }
}
