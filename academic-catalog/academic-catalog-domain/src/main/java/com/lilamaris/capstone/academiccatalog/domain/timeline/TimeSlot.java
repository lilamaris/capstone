package com.lilamaris.capstone.academiccatalog.domain.timeline;

import com.lilamaris.capstone.academiccatalog.domain.shared.time.EmbeddableTemporalRange;
import com.lilamaris.capstone.academiccatalog.domain.shared.time.TemporalRange;
import com.lilamaris.capstone.kernel.core.condition.Preconditions;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "time_slot")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "timeline_id")
    private Timeline timeline;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startAt", column = @Column(name = "tx_start_at", nullable = false)),
            @AttributeOverride(name = "endAt", column = @Column(name = "tx_end_at", nullable = false))
    })
    private EmbeddableTemporalRange txRange;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startAt", column = @Column(name = "op_start_at", nullable = false)),
            @AttributeOverride(name = "endAt", column = @Column(name = "op_end_at", nullable = false))
    })
    private EmbeddableTemporalRange opRange;

    @Column(name = "description", nullable = false)
    private String description;

    private TimeSlot(Timeline timeline, EmbeddableTemporalRange txRange, EmbeddableTemporalRange opRange, String description) {
        this.timeline = Preconditions.requireNonNull(timeline, "timeline");
        this.txRange = Preconditions.requireNonNull(txRange, "txRange");
        this.opRange = Preconditions.requireNonNull(opRange, "opRange");
        this.description = Preconditions.requireNonBlank(description, "description");
    }

    public static TimeSlot of(Timeline timeline, TemporalRange txRange, TemporalRange opRange, String description) {
        return new TimeSlot(
                timeline,
                EmbeddableTemporalRange.from(Preconditions.requireNonNull(txRange, "txRange")),
                EmbeddableTemporalRange.from(Preconditions.requireNonNull(opRange, "opRange")),
                description
        );
    }

    public void updateTimeline(Timeline timeline) {
        this.timeline = Preconditions.requireNonNull(timeline, "timeline");
    }

    public void updateTxRange(TemporalRange txRange) {
        this.txRange = EmbeddableTemporalRange.from(Preconditions.requireNonNull(txRange, "txRange"));
    }

    public void updateOpRange(TemporalRange opRange) {
        this.opRange = EmbeddableTemporalRange.from(Preconditions.requireNonNull(opRange, "opRange"));
    }

    public void updateDescription(String description) {
        this.description = Preconditions.requireNonBlank(description, "description");
    }
}
