package com.lilamaris.capstone.resource.domain;

import com.lilamaris.capstone.resource.domain.event.ResourceCreatedEvent;
import com.lilamaris.capstone.resource.domain.event.ResourceRemovedEvent;
import com.lilamaris.capstone.resource.domain.id.ResourceId;
import com.lilamaris.capstone.shared.domain.contract.Auditable;
import com.lilamaris.capstone.shared.domain.event.DomainEvent;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.metadata.AuditMetadata;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaAuditMetadata;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaDomainRef;
import com.lilamaris.capstone.shared.domain.type.DomainType;
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
@Table(name = "resource")
@EntityListeners(AuditingEntityListener.class)
public class Resource implements Persistable<ResourceId>, Auditable {
    @Embedded
    private final JpaAuditMetadata audit = new JpaAuditMetadata();

    @Transient
    private final List<DomainEvent> eventList = new ArrayList<>();

    @Transient
    private boolean isNew = true;

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private ResourceId id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "type.name", column = @Column(name = "resource_type")),
            @AttributeOverride(name = "id", column = @Column(name = "resource_id"))
    })
    private JpaDomainRef resource;

    protected Resource(
            ResourceId id,
            JpaDomainRef resource
    ) {
        this.id = requireField(id, "id");
        this.resource = requireField(resource, "resource");
    }

    public static Resource create(
            Supplier<ResourceId> idSupplier,
            DomainType resourceType,
            ExternalizableId resourceId
    ) {
        var resource = new Resource();
        resource.id = idSupplier.get();
        resource.resource = JpaDomainRef.from(resourceType, resourceId);
        resource.registerCreated();
        return resource;
    }

    public ExternalizableId getResourceId() {
        return resource.id();
    }

    public DomainType getResourceType() {
        return resource.type();
    }

    private void registerCreated() {
        var event = new ResourceCreatedEvent(resource, Instant.now());
        this.eventList.add(event);
    }

    private void registerRemoved() {
        var event = new ResourceRemovedEvent(resource, Instant.now());
        this.eventList.add(event);
    }

    @Override
    public AuditMetadata auditMetadata() {
        return audit;
    }

    @Override
    public ResourceId getId() {
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
