package com.lilamaris.capstone.snapshot.domain;

import com.lilamaris.capstone.shared.domain.contract.Auditable;
import com.lilamaris.capstone.shared.domain.contract.Identifiable;
import com.lilamaris.capstone.shared.domain.event.DomainEvent;
import com.lilamaris.capstone.shared.domain.metadata.AuditMetadata;
import com.lilamaris.capstone.shared.domain.persistence.persistence.jpa.JpaAuditMetadata;
import com.lilamaris.capstone.shared.domain.persistence.persistence.jpa.JpaDomainRef;
import com.lilamaris.capstone.snapshot.domain.event.SnapshotDeltaCreated;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotDeltaId;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.lilamaris.capstone.shared.domain.util.Validation.requireField;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "snapshot_delta")
@EntityListeners(AuditingEntityListener.class)
public class SnapshotDelta implements Identifiable<SnapshotDeltaId>, Auditable {
    @Embedded
    private final JpaAuditMetadata audit = new JpaAuditMetadata();

    @Transient
    private final List<DomainEvent> eventList = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private SnapshotDeltaId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "snapshot_id", insertable = false, nullable = false, updatable = false))
    private SnapshotId snapshotId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "type", column = @Column(name = "resource_type")),
            @AttributeOverride(name = "id", column = @Column(name = "resource_id"))
    })
    private JpaDomainRef resourceRef;

    private String jsonPatch;

    protected SnapshotDelta(SnapshotDeltaId id, SnapshotId snapshotId, JpaDomainRef resourceRef, String jsonPatch) {
        this.id = requireField(id, "id");
        this.snapshotId = requireField(snapshotId, "snapshotId");
        this.resourceRef = requireField(resourceRef, "resourceRef");
        this.jsonPatch = requireField(jsonPatch, "jsonPatch");
    }

    protected static SnapshotDelta create(SnapshotDeltaId id, SnapshotId snapshotId, JpaDomainRef resourceRef, String jsonPatch) {
        var delta = new SnapshotDelta(id, snapshotId, resourceRef, jsonPatch);
        delta.registerCreated();
        return delta;
    }

    private void registerCreated() {
        var event = new SnapshotDeltaCreated(id, resourceRef.toPOJO(), Instant.now());
        eventList.add(event);
    }

    @Override
    public final SnapshotDeltaId id() {
        return id;
    }

    @Override
    public AuditMetadata auditMetadata() {
        return audit;
    }
}
