package com.lilamaris.capstone.resource_offer.domain;

import com.lilamaris.capstone.resource_offer.domain.id.ResourceOfferId;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.lilamaris.capstone.shared.domain.util.Validation.requireField;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "resource_offer")
@EntityListeners(AuditingEntityListener.class)
public class ResourceOffer implements
        Persistable<ResourceOfferId>,
        Identifiable<ResourceOfferId>,
        Auditable {
    @Embedded
    private final JpaAuditMetadata audit = new JpaAuditMetadata();

    @Transient
    private final List<DomainEvent> eventList = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private ResourceOfferId id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "type.name", column = @Column(name = "resource_type")),
            @AttributeOverride(name = "id", column = @Column(name = "resource_id"))
    })
    private JpaDomainRef resource;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "snapshot_id", nullable = false))
    private JpaExternalizableId snapshotId;
    @Transient
    private boolean isNew = true;

    protected ResourceOffer(
            ResourceOfferId id,
            JpaDomainRef resource,
            JpaExternalizableId snapshotId
    ) {
        this.id = requireField(id, "id");
        this.resource = requireField(resource, "resource");
        this.snapshotId = requireField(snapshotId, "snapshotId");
    }

    public static ResourceOffer create(
            Supplier<ResourceOfferId> idSupplier,
            DomainRef resourceRef,
            ExternalizableId externalSnapshotId
    ) {
        var resource = JpaDomainRef.from(resourceRef);
        var snapshotId = JpaExternalizableId.from(externalSnapshotId);
        var resourceOffer = new ResourceOffer(
                idSupplier.get(),
                resource,
                snapshotId
        );
        resourceOffer.registerCreated();
        return resourceOffer;
    }

    private void registerCreated() {

    }

    @Override
    public ResourceOfferId id() {
        return id;
    }

    @Override
    public AuditMetadata auditMetadata() {
        return audit;
    }

    @Override
    public ResourceOfferId getId() {
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
