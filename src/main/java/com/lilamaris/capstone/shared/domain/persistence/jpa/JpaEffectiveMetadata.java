package com.lilamaris.capstone.shared.domain.persistence.jpa;

import com.lilamaris.capstone.shared.domain.defaults.DefaultEffectiveMetadata;
import com.lilamaris.capstone.shared.domain.exception.DomainIllegalArgumentException;
import com.lilamaris.capstone.shared.domain.metadata.EffectiveMetadata;
import com.lilamaris.capstone.shared.domain.defaults.DefaultEffectiveSplitEntry;
import com.lilamaris.capstone.shared.domain.persistence.ToPojo;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

import static com.lilamaris.capstone.shared.domain.util.Validation.requireField;

@Getter
@ToString
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JpaEffectiveMetadata implements EffectiveMetadata, ToPojo<EffectiveMetadata> {
    private Instant from;
    private Instant to;

    private JpaEffectiveMetadata(Instant from, Instant to) {
        this.from = requireField(from, "from");
        this.to = requireField(to, "to");
        checkInvariant(from, to);
    }

    public static JpaEffectiveMetadata create(Instant from, Instant to) {
        return new JpaEffectiveMetadata(from, to);
    }

    public static JpaEffectiveMetadata create(Instant from) {
        return new JpaEffectiveMetadata(from, MAX);
    }

    public static JpaEffectiveMetadata create(LocalDateTime from, LocalDateTime to, ZoneId zoneId) {
        var parsedFrom = from.atZone(zoneId).toInstant();
        var parsedTo = to.atZone(zoneId).toInstant();
        return new JpaEffectiveMetadata(parsedFrom, parsedTo);
    }

    public static JpaEffectiveMetadata from(EffectiveMetadata effectiveMetadata) {
        return new JpaEffectiveMetadata(effectiveMetadata.from(), effectiveMetadata.to());
    }

    private static void checkInvariant(Instant from, Instant to) {
        if (to.isBefore(from)) throw new DomainIllegalArgumentException("Field 'to' must not be before 'from'.");
    }

    @Override
    public Instant from() {
        return from;
    }

    @Override
    public Instant to() {
        return to;
    }

    public void open(Instant at) {
        this.from = at;
    }

    public void close(Instant at) {
        this.to = at;
    }

    public DefaultEffectiveSplitEntry splitAt(Instant at) {
        var left = JpaEffectiveMetadata.create(from, at);
        var right = JpaEffectiveMetadata.create(at, to);
        return new DefaultEffectiveSplitEntry(left, right);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof JpaEffectiveMetadata other
                && from.equals(other.from)
                && to.equals(other.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public EffectiveMetadata toPOJO() {
        return new DefaultEffectiveMetadata(from, to);
    }
}
