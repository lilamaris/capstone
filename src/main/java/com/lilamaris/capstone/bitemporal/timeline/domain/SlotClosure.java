package com.lilamaris.capstone.bitemporal.timeline.domain;

import com.lilamaris.capstone.bitemporal.timeline.domain.event.SlotClosureCreated;
import com.lilamaris.capstone.bitemporal.timeline.domain.id.SlotClosureId;
import com.lilamaris.capstone.bitemporal.timeline.domain.id.SlotId;
import com.lilamaris.capstone.bitemporal.timeline.domain.id.TimelineId;
import com.lilamaris.capstone.shared.domain.contract.Identifiable;
import com.lilamaris.capstone.shared.domain.event.DomainEvent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Persistable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.lilamaris.capstone.shared.domain.util.Validation.requireField;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "timeline_slot_closure")
public class SlotClosure implements Persistable<SlotClosureId>, Identifiable<SlotClosureId> {
    @Transient
    private final List<DomainEvent> eventList = new ArrayList<>();
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private SlotClosureId id;
    @Getter
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "timeline_id", insertable = false, nullable = false, updatable = false))
    private TimelineId timelineId;

    @Getter
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "ancestor_slot_id", nullable = false, updatable = false))
    private SlotId ancestorSlotId;

    @Getter
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "descendant_slot_id", nullable = false, updatable = false))
    private SlotId descendantSlotId;

    @Getter
    private Integer depth;
    @Transient
    private boolean isNew = true;

    protected SlotClosure(
            SlotClosureId id,
            TimelineId timelineId,
            SlotId ancestorSlotId,
            SlotId descendantSlotId,
            Integer depth
    ) {
        this.id = requireField(id, "id");
        this.timelineId = requireField(timelineId, "timelineId");
        this.ancestorSlotId = requireField(ancestorSlotId, "ancestorSlotId");
        this.descendantSlotId = requireField(descendantSlotId, "descendantSlotId");
        this.depth = requireField(depth, "depth");
    }

    protected static SlotClosure create(
            Supplier<SlotClosureId> idSupplier,
            TimelineId timelineId,
            SlotId ancestorSlotId,
            SlotId descendantSlotId,
            Integer depth
    ) {
        var id = idSupplier.get();
        var slotClosure = new SlotClosure(
                id,
                timelineId,
                ancestorSlotId,
                descendantSlotId,
                depth
        );
        slotClosure.registerCreated();
        return slotClosure;
    }

    private void registerCreated() {
        var event = new SlotClosureCreated(
                id,
                ancestorSlotId,
                descendantSlotId,
                Instant.now()
        );
        eventList.add(event);
    }

    protected List<DomainEvent> pullEvent() {
        var copy = List.copyOf(eventList);
        eventList.clear();
        return copy;
    }

    @Override
    public SlotClosureId id() {
        return id;
    }

    @Override
    public SlotClosureId getId() {
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
