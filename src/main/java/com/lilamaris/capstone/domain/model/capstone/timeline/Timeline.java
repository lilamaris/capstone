package com.lilamaris.capstone.domain.model.capstone.timeline;

import com.lilamaris.capstone.domain.exception.DomainIllegalStateException;
import com.lilamaris.capstone.domain.model.capstone.timeline.embed.Effective;
import com.lilamaris.capstone.domain.model.capstone.timeline.embed.EffectiveSelector;
import com.lilamaris.capstone.domain.model.capstone.timeline.event.TimelineCreated;
import com.lilamaris.capstone.domain.model.capstone.timeline.exception.TimelineDomainException;
import com.lilamaris.capstone.domain.model.capstone.timeline.exception.TimelineErrorCode;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotSlotId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
import com.lilamaris.capstone.domain.model.common.domain.contract.Auditable;
import com.lilamaris.capstone.domain.model.common.domain.contract.Describable;
import com.lilamaris.capstone.domain.model.common.domain.contract.Identifiable;
import com.lilamaris.capstone.domain.model.common.domain.event.DomainEvent;
import com.lilamaris.capstone.domain.model.common.domain.event.aggregate.CollectedDomainEvent;
import com.lilamaris.capstone.domain.model.common.domain.metadata.AuditMetadata;
import com.lilamaris.capstone.domain.model.common.domain.metadata.DescriptionMetadata;
import com.lilamaris.capstone.domain.model.common.infra.persistence.jpa.JpaAuditMetadata;
import com.lilamaris.capstone.domain.model.common.infra.persistence.jpa.JpaDescriptionMetadata;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.lilamaris.capstone.domain.model.util.Validation.requireField;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "timeline_root")
@EntityListeners(AuditingEntityListener.class)
public class Timeline implements Identifiable<TimelineId>, Describable, Auditable {
    @Embedded
    private final JpaAuditMetadata audit = new JpaAuditMetadata();
    @Transient
    private final List<DomainEvent> eventList = new ArrayList<>();
    @Getter(AccessLevel.NONE)
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private TimelineId id;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "timeline_id", nullable = false)
    private List<SnapshotSlot> slotList;
    @Embedded
    private JpaDescriptionMetadata descriptionMetadata;

    protected Timeline(
            TimelineId id,
            List<SnapshotSlot> slotList,
            JpaDescriptionMetadata descriptionMetadata
    ) {
        this.id = requireField(id, "id");
        this.slotList = requireField(slotList, "slotList");
        this.descriptionMetadata = requireField(descriptionMetadata, "descriptionMetadata");
    }

    public static Timeline create(
            Supplier<TimelineId> idSupplier,
            Supplier<SnapshotSlotId> snapshotSlotIdSupplier,
            String title,
            String details,
            Instant txAt,
            Instant initialValidAt
    ) {
        var id = idSupplier.get();
        var descriptionMetadata = JpaDescriptionMetadata.create(title, details);
        var timeline = new Timeline(
                id,
                new ArrayList<>(),
                descriptionMetadata
        );
        timeline.createInitialSlot(snapshotSlotIdSupplier, txAt, initialValidAt);
        timeline.registerCreated();
        return timeline;
    }

    @DomainEvents
    CollectedDomainEvent domainEvents() {
        return new CollectedDomainEvent(List.copyOf(eventList));
    }

    @AfterDomainEventPublication
    void clearDomainEvents() {
        eventList.clear();
    }

    private void registerCreated() {
        var event = new TimelineCreated(id, Instant.now());
        eventList.add(event);
    }

    @Override
    public final TimelineId id() {
        return id;
    }

    @Override
    public DescriptionMetadata descriptionMetadata() {
        return descriptionMetadata;
    }

    @Override
    public AuditMetadata auditMetadata() {
        return audit;
    }

    @Override
    public void updateDescription(DescriptionMetadata descriptionMetadata) {
        this.descriptionMetadata = JpaDescriptionMetadata.from(descriptionMetadata);
    }

    private void createInitialSlot(
            Supplier<SnapshotSlotId> snapshotSlotIdSupplier,
            Instant txAt,
            Instant validAt
    ) {
        var tx = Effective.create(txAt, Effective.MAX);
        var valid = Effective.create(validAt, Effective.MAX);
        var snapshotSlot = SnapshotSlot.create(snapshotSlotIdSupplier.get(), id, tx, valid);
        var events = snapshotSlot.pullEvent();
        this.slotList.add(snapshotSlot);
        eventList.addAll(events);
    }

    public void migrate(
            Supplier<SnapshotSlotId> snapshotSlotIdSupplier,
            Instant txAt,
            Instant validAt
    ) {
        checkAnySlotExists();

        var maybeExactSlot = slotList.stream()
                .filter(SnapshotSlot.ifEffectiveOpenEqualTo(EffectiveSelector.TX, true))
                .filter(SnapshotSlot.ifEffectiveContains(EffectiveSelector.VALID, validAt))
                .toList();

        if (maybeExactSlot.size() > 1) {
            throw new DomainIllegalStateException(
                    String.format("More then one snapshot exists for the same valid at: '%s'", validAt)
            );
        }

        var parentSlot = maybeExactSlot.stream().findFirst().orElseThrow(() -> new TimelineDomainException(
                TimelineErrorCode.NO_AVAILABLE_SLOT,
                String.format(
                        "No slot match the given criteria valid '%s'", validAt
                )
        ));

        parentSlot.close(EffectiveSelector.TX, txAt);
        eventList.addAll(parentSlot.pullEvent());

        var newTx = Effective.create(txAt, Effective.MAX);
        var newValidSplit = parentSlot.getValid().splitAt(validAt);
        var newValidLeft = newValidSplit.left();
        var newValidRight = newValidSplit.right();

        var slotLeft = SnapshotSlot.create(
                snapshotSlotIdSupplier.get(),
                id,
                parentSlot.id(),
                newTx,
                newValidLeft
        );
        slotList.add(slotLeft);
        eventList.addAll(slotLeft.pullEvent());

        var slotRight = SnapshotSlot.create(
                snapshotSlotIdSupplier.get(),
                id,
                slotLeft.id(),
                newTx,
                newValidRight
        );
        slotList.add(slotRight);
        eventList.addAll(slotRight.pullEvent());
    }

    public void merge(
            Supplier<SnapshotSlotId> snapshotSlotIdSupplier,
            Instant txAt,
            Instant validFrom,
            Instant validTo
    ) {
        checkAnySlotExists();

        var validRange = Effective.create(validFrom, validTo);

        var candidateSlots = slotList.stream()
                .filter(SnapshotSlot.ifEffectiveOpenEqualTo(EffectiveSelector.TX, true))
                .filter(SnapshotSlot.ifEffectiveOverlap(EffectiveSelector.VALID, validRange))
                .sorted(SnapshotSlot.sortAsc(EffectiveSelector.VALID))
                .toList();

        if (candidateSlots.isEmpty()) {
            throw new TimelineDomainException(TimelineErrorCode.NO_AVAILABLE_SLOT, "No slot matched the given criteria valid range" + validRange);
        }

        candidateSlots.forEach(slot -> {
            slot.close(EffectiveSelector.TX, txAt);
            eventList.addAll(slot.pullEvent());
        });

        var earliestSlot = candidateSlots.getFirst();
        var latestSlot = candidateSlots.getLast();

        var mergedTx = Effective.create(txAt, Effective.MAX);
        var mergedValid = Effective.create(
                earliestSlot.getValid().getFrom(),
                latestSlot.getValid().getTo()
        );
        var mergedSlot = SnapshotSlot.create(
                snapshotSlotIdSupplier.get(),
                id,
                mergedTx,
                mergedValid
        );
        slotList.add(mergedSlot);
        eventList.addAll(mergedSlot.pullEvent());
    }

    private void checkAnySlotExists() {
        if (slotList.isEmpty()) {
            throw new TimelineDomainException(TimelineErrorCode.EMPTY_SLOT, "No slot exists on this timeline.");
        }
    }
}

