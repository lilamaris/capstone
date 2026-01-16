package com.lilamaris.capstone.snapshot.domain;

import com.lilamaris.capstone.shared.domain.contract.Auditable;
import com.lilamaris.capstone.shared.domain.contract.Describable;
import com.lilamaris.capstone.shared.domain.contract.Identifiable;
import com.lilamaris.capstone.shared.domain.event.DomainEvent;
import com.lilamaris.capstone.shared.domain.metadata.AuditMetadata;
import com.lilamaris.capstone.shared.domain.metadata.DescriptionMetadata;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaAuditMetadata;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaDescriptionMetadata;
import com.lilamaris.capstone.snapshot.domain.event.SnapshotCreated;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.lilamaris.capstone.shared.domain.util.Validation.requireField;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "snapshot")
@EntityListeners(AuditingEntityListener.class)
public class Snapshot implements Persistable<SnapshotId>, Identifiable<SnapshotId>, Describable, Auditable {
    @Embedded
    private final JpaAuditMetadata audit = new JpaAuditMetadata();

    @Transient
    private final List<DomainEvent> eventList = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private SnapshotId id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "snapshot_id", nullable = false)
    private List<SnapshotDelta> snapshotDeltaList;

    @Embedded
    private JpaDescriptionMetadata descriptionMetadata;

    protected Snapshot(
            SnapshotId id,
            List<SnapshotDelta> snapshotDeltaList,
            JpaDescriptionMetadata descriptionMetadata
    ) {
        this.id = requireField(id, "id");
        this.snapshotDeltaList = requireField(snapshotDeltaList, "snapshotDeltaList");
        this.descriptionMetadata = requireField(descriptionMetadata, "descriptionMetadata");
    }

    public static Snapshot create(
            Supplier<SnapshotId> idSupplier,
            String title,
            String details
    ) {
        var descriptionMetadata = JpaDescriptionMetadata.create(title, details);
        var snapshot = new Snapshot(
                idSupplier.get(),
                new ArrayList<>(),
                descriptionMetadata
        );
        snapshot.registerCreated();
        return snapshot;
    }

    @Override
    public final SnapshotId id() {
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

    protected List<DomainEvent> pullEvent() {
        var copy = List.copyOf(eventList);
        eventList.clear();
        return copy;
    }

    private void registerCreated() {
        var event = new SnapshotCreated(id, Instant.now());
        eventList.add(event);
    }

    @Transient
    private boolean isNew = true;

    @Override
    public SnapshotId getId() {
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
