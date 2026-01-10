package com.lilamaris.capstone.access_control.domain;

import com.lilamaris.capstone.access_control.domain.id.AccessControlId;
import com.lilamaris.capstone.shared.domain.contract.Auditable;
import com.lilamaris.capstone.shared.domain.contract.Identifiable;
import com.lilamaris.capstone.shared.domain.event.DomainEvent;
import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.metadata.AuditMetadata;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaActor;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaAuditMetadata;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaDomainRef;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.lilamaris.capstone.shared.domain.util.Validation.requireField;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "access_control")
@EntityListeners(AuditingEntityListener.class)
public class AccessControl implements Identifiable<AccessControlId>, Auditable {
    @Embedded
    private final JpaAuditMetadata audit = new JpaAuditMetadata();

    @Transient
    private final List<DomainEvent> eventList = new ArrayList<>();

    @Getter(AccessLevel.NONE)
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private AccessControlId id;

    @Embedded
    private JpaActor actor;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "type.name", column = @Column(name = "resource_type", nullable = false)),
            @AttributeOverride(name = "id", column = @Column(name = "resource_id", nullable = false))
    })
    private JpaDomainRef resource;

    private String scopedRole;

    protected AccessControl(
            AccessControlId id,
            JpaActor actor,
            JpaDomainRef resource,
            String scopedRole
    ) {
        this.id = requireField(id, "id");
        this.actor = requireField(actor, "actor");
        this.resource = requireField(resource, "resource");
        this.scopedRole = requireField(scopedRole, "scopedRole");
    }

    public static AccessControl create(
            CanonicalActor actor,
            DomainRef resourceRef,
            String scopedRole,
            Supplier<AccessControlId> idSupplier
    ) {
        var act = JpaActor.from(actor);
        var ref = JpaDomainRef.from(resourceRef);
        var accessControl = new AccessControl(idSupplier.get(), act, ref, scopedRole);
        accessControl.registerCreated();
        return accessControl;
    }

    private void registerCreated() {

    }

    @Override
    public AccessControlId id() {
        return id;
    }

    @Override
    public AuditMetadata auditMetadata() {
        return audit;
    }
}
