package com.lilamaris.capstone.timeline.domain;

import com.lilamaris.capstone.shared.domain.contract.Auditable;
import com.lilamaris.capstone.shared.domain.contract.Describable;
import com.lilamaris.capstone.shared.domain.contract.Identifiable;
import com.lilamaris.capstone.shared.domain.event.DomainEvent;
import com.lilamaris.capstone.shared.domain.event.aggregate.CollectedDomainEvent;
import com.lilamaris.capstone.shared.domain.exception.DomainIllegalStateException;
import com.lilamaris.capstone.shared.domain.metadata.AuditMetadata;
import com.lilamaris.capstone.shared.domain.metadata.DescriptionMetadata;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaAuditMetadata;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaDescriptionMetadata;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaEffectiveMetadata;
import com.lilamaris.capstone.timeline.domain.event.TimelineCreated;
import com.lilamaris.capstone.timeline.domain.exception.TimelineDomainException;
import com.lilamaris.capstone.timeline.domain.exception.TimelineErrorCode;
import com.lilamaris.capstone.timeline.domain.id.SlotClosureId;
import com.lilamaris.capstone.timeline.domain.id.SlotId;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.lilamaris.capstone.shared.domain.util.Validation.requireField;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "timeline")
@EntityListeners(AuditingEntityListener.class)
public class Timeline implements Persistable<TimelineId>, Identifiable<TimelineId>, Describable, Auditable {
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
    private List<Slot> slotList;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "timeline_id", nullable = false)
    private List<SlotClosure> slotClosureList;

    @Embedded
    private JpaDescriptionMetadata descriptionMetadata;
    @Transient
    private boolean isNew = true;

    protected Timeline(
            TimelineId id,
            List<Slot> slotList,
            List<SlotClosure> slotClosureList,
            JpaDescriptionMetadata descriptionMetadata
    ) {
        this.id = requireField(id, "id");
        this.slotList = requireField(slotList, "slotList");
        this.slotClosureList = requireField(slotClosureList, "slotClosureList");
        this.descriptionMetadata = requireField(descriptionMetadata, "descriptionMetadata");
    }

    public static Timeline create(
            Supplier<TimelineId> idSupplier,
            Supplier<SlotId> slotIdSupplier,
            Supplier<SlotClosureId> slotClosureIdSupplier,
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
                new ArrayList<>(),
                descriptionMetadata
        );
        timeline.createInitialSlot(slotIdSupplier, slotClosureIdSupplier, txAt, initialValidAt);
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
            Supplier<SlotId> slotIdSupplier,
            Supplier<SlotClosureId> slotClosureIdSupplier,
            Instant txAt,
            Instant validAt
    ) {
        var tx = JpaEffectiveMetadata.create(txAt);
        var valid = JpaEffectiveMetadata.create(validAt);
        var slot = Slot.create(slotIdSupplier, id, tx, valid);
        var slotClosure = SlotClosure.create(slotClosureIdSupplier, id, slot.id(), slot.id(), 0);
        slotList.add(slot);
        slotClosureList.add(slotClosure);
        eventList.addAll(Stream.concat(slot.pullEvent().stream(), slotClosure.pullEvent().stream()).toList());
    }

    public void migrate(
            Supplier<SlotId> slotIdSupplier,
            Supplier<SlotClosureId> slotClosureIdSupplier,
            Instant txAt,
            Instant validAt
    ) {
        checkAnySlotExists();

        var maybeExactSlot = slotList.stream()
                .filter(Slot.ifEffectiveOpenEqualTo(EffectiveSelector.TX, true))
                .filter(Slot.ifEffectiveContains(EffectiveSelector.VALID, validAt))
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

        var newTx = JpaEffectiveMetadata.create(txAt, JpaEffectiveMetadata.MAX);
        var newValidSplit = parentSlot.getValid().splitAt(validAt);
        var newValidLeft = JpaEffectiveMetadata.from(newValidSplit.left());
        var newValidRight = JpaEffectiveMetadata.from(newValidSplit.right());

        createSlot(
                slotIdSupplier,
                slotClosureIdSupplier,
                newTx,
                newValidLeft,
                parentSlot.id()
        );

        createSlot(
                slotIdSupplier,
                slotClosureIdSupplier,
                newTx,
                newValidRight,
                parentSlot.id()
        );
    }

    public void merge(
            Supplier<SlotId> slotIdSupplier,
            Supplier<SlotClosureId> slotClosureIdSupplier,
            Instant txAt,
            Instant validFrom,
            Instant validTo
    ) {
        checkAnySlotExists();

        var validRange = JpaEffectiveMetadata.create(validFrom, validTo);

        var candidateSlots = slotList.stream()
                .filter(Slot.ifEffectiveOpenEqualTo(EffectiveSelector.TX, true))
                .filter(Slot.ifEffectiveOverlap(EffectiveSelector.VALID, validRange))
                .sorted(Slot.sortAsc(EffectiveSelector.VALID))
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

        var mergedTx = JpaEffectiveMetadata.create(txAt, JpaEffectiveMetadata.MAX);
        var mergedValid = JpaEffectiveMetadata.create(
                earliestSlot.getValid().getFrom(),
                latestSlot.getValid().getTo()
        );
        createSlot(
                slotIdSupplier,
                slotClosureIdSupplier,
                mergedTx,
                mergedValid,
                latestSlot.id()
        );
    }

    private void createSlot(
            Supplier<SlotId> slotIdSupplier,
            Supplier<SlotClosureId> slotClosureIdSupplier,
            JpaEffectiveMetadata tx,
            JpaEffectiveMetadata valid,
            SlotId ancestorSlotId
    ) {
        var slot = Slot.create(
                slotIdSupplier,
                id,
                tx,
                valid
        );
        var reflectClosures = slotClosureList.stream()
                .filter(c -> c.getDescendantSlotId().equals(ancestorSlotId))
                .map(c -> SlotClosure.create(
                        slotClosureIdSupplier,
                        id,
                        c.getAncestorSlotId(),
                        slot.id(),
                        c.getDepth()
                ))
                .toList();
        slotClosureList.addAll(reflectClosures);

        var maxDepth = reflectClosures.stream()
                .max(Comparator.comparing(SlotClosure::getDepth))
                .map(SlotClosure::getDepth)
                .orElse(0);

        var closure = SlotClosure.create(
                slotClosureIdSupplier,
                id,
                slot.id(),
                slot.id(),
                maxDepth + 1
        );
        slotClosureList.add(closure);
        slotList.add(slot);
        eventList.addAll(slot.pullEvent());
    }

    private void checkAnySlotExists() {
        if (slotList.isEmpty()) {
            throw new TimelineDomainException(TimelineErrorCode.EMPTY_SLOT, "No slot exists on this timeline.");
        }
    }

    @Override
    public TimelineId getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostLoad
    @PostPersist
    private void markNotNew() {
        this.isNew = false;
    }
}

