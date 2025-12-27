package com.lilamaris.capstone.domain.model.auth.access_control;

import com.lilamaris.capstone.domain.model.auth.access_control.id.AccessControlId;
import com.lilamaris.capstone.domain.model.common.domain.contract.Identifiable;
import com.lilamaris.capstone.domain.model.common.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.domain.model.common.domain.event.aggregate.AggregateEvent;
import com.lilamaris.capstone.domain.model.common.domain.id.DomainRef;
import com.lilamaris.capstone.domain.model.common.infra.persistence.jpa.JpaActor;
import com.lilamaris.capstone.domain.model.common.infra.persistence.jpa.JpaAuditMetadata;
import com.lilamaris.capstone.domain.model.common.infra.persistence.jpa.JpaDomainRef;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;
import java.util.function.Supplier;

import static com.lilamaris.capstone.domain.model.util.Validation.requireField;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "_access_control")
@EntityListeners(AuditingEntityListener.class)
public class AccessControl implements Identifiable<AccessControlId> {
    @Embedded
    private final JpaAuditMetadata audit = new JpaAuditMetadata();
    @Getter(AccessLevel.NONE)
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private AccessControlId id;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "type", column = @Column(name = "actor_type")),
            @AttributeOverride(name = "id", column = @Column(name = "actor_id"))
    })
    private JpaActor actor;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "type", column = @Column(name = "resource_type")),
            @AttributeOverride(name = "id", column = @Column(name = "resource_id"))
    })
    private JpaDomainRef resource;
    private String scopedRole;

    @Transient
    private List<AggregateEvent> eventList;

    protected AccessControl(
            AccessControlId id,
            JpaActor actor,
            JpaDomainRef resource,
            String scopedRole
    ) {
        this.id = requireField(id, "id");
        this.actor = requireField(actor, "actor");
        this.resource = requireField(resource, "resourceRef");
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
}
