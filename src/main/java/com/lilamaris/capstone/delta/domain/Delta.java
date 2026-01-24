package com.lilamaris.capstone.delta.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonpatch.JsonPatch;
import com.lilamaris.capstone.delta.domain.event.DeltaCreated;
import com.lilamaris.capstone.delta.domain.id.DeltaId;
import com.lilamaris.capstone.shared.application.jsonPatch.JsonPatchEngine;
import com.lilamaris.capstone.shared.domain.contract.Auditable;
import com.lilamaris.capstone.shared.domain.contract.Identifiable;
import com.lilamaris.capstone.shared.domain.event.DomainEvent;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.metadata.AuditMetadata;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaAuditMetadata;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaDomainRef;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaExternalizableId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.lilamaris.capstone.shared.domain.util.Validation.requireField;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "delta")
@EntityListeners(AuditingEntityListener.class)
public class Delta implements Persistable<DeltaId>, Identifiable<DeltaId>, Auditable {
    @Embedded
    private final JpaAuditMetadata audit = new JpaAuditMetadata();

    @Transient
    private final List<DomainEvent> eventList = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private DeltaId id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "slot_id", nullable = false))
    private JpaExternalizableId slotId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "type", column = @Column(name = "resource_type")),
            @AttributeOverride(name = "id", column = @Column(name = "resource_id"))
    })
    private JpaDomainRef resource;

    private String state;

    private String patch;

    @Transient
    private boolean isNew = true;

    protected Delta(
            DeltaId id,
            JpaExternalizableId slotId,
            JpaDomainRef resource,
            String state,
            String patch
    ) {
        this.id = requireField(id, "id");
        this.slotId = requireField(slotId, "slotId");
        this.resource = requireField(resource, "resource");
        this.state = requireField(state, "state");
        this.patch = requireField(patch, "patch");
    }

    public static Delta create(
            JsonPatchEngine jsonPatchEngine,
            Supplier<DeltaId> idSupplier,
            ExternalizableId slotId,
            DomainRef resource,
            @Nullable JsonNode state,
            @Nullable JsonPatch patch
    ) {
        var id = idSupplier.get();
        var externalSlotId = JpaExternalizableId.from(slotId);
        var resourceRef = JpaDomainRef.from(resource);
        var stringifyState = jsonPatchEngine.stringify(state);
        var stringifyPatch = jsonPatchEngine.stringify(patch);
        var delta = new Delta(
                id,
                externalSlotId,
                resourceRef,
                stringifyState,
                stringifyPatch
        );
        delta.registerCreated();
        return delta;
    }

    private void registerCreated() {
        var event = new DeltaCreated(id, resource.toPOJO(), Instant.now());
        eventList.add(event);
    }

    protected List<DomainEvent> pullEvent() {
        var copy = List.copyOf(eventList);
        eventList.clear();
        return copy;
    }

    @Override
    public final DeltaId id() {
        return id;
    }

    @Override
    public AuditMetadata auditMetadata() {
        return audit;
    }

    @Override
    public DeltaId getId() {
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
