package com.lilamaris.capstone.academiccatalog.domain.timeline;

import com.lilamaris.capstone.academiccatalog.domain.shared.time.EmbeddableInstantRange;
import com.lilamaris.capstone.academiccatalog.domain.shared.time.InstantRange;
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
    private EmbeddableInstantRange txRange;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startAt", column = @Column(name = "op_start_at", nullable = false)),
            @AttributeOverride(name = "endAt", column = @Column(name = "op_end_at", nullable = false))
    })
    private EmbeddableInstantRange opRange;

    @Column(name = "description", nullable = false)
    private String description;

    private TimeSlot(Timeline timeline, EmbeddableInstantRange txRange, EmbeddableInstantRange opRange, String description) {
        this.timeline = Preconditions.requireNonNull(timeline, "timeline");
        this.txRange = Preconditions.requireNonNull(txRange, "txRange");
        this.opRange = Preconditions.requireNonNull(opRange, "opRange");
        this.description = Preconditions.requireNonBlank(description, "description");
    }

    public static TimeSlot of(Timeline timeline, InstantRange txRange, InstantRange opRange, String description) {
        return new TimeSlot(
                timeline,
                EmbeddableInstantRange.from(Preconditions.requireNonNull(txRange, "txRange")),
                EmbeddableInstantRange.from(Preconditions.requireNonNull(opRange, "opRange")),
                description
        );
    }

    public void updateTimeline(Timeline timeline) {
        this.timeline = Preconditions.requireNonNull(timeline, "timeline");
    }

    public void updateTxRange(InstantRange txRange) {
        this.txRange = EmbeddableInstantRange.from(Preconditions.requireNonNull(txRange, "txRange"));
    }

    public void updateOpRange(InstantRange opRange) {
        this.opRange = EmbeddableInstantRange.from(Preconditions.requireNonNull(opRange, "opRange"));
    }

    public void updateDescription(String description) {
        this.description = Preconditions.requireNonBlank(description, "description");
    }
}
