package com.lilamaris.capstone.membership.domain;

import com.lilamaris.capstone.membership.domain.id.MembershipId;
import com.lilamaris.capstone.shared.domain.contract.Auditable;
import com.lilamaris.capstone.shared.domain.contract.Identifiable;
import com.lilamaris.capstone.shared.domain.event.actor.CanonicalActor;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.metadata.AuditMetadata;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaActor;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaAuditMetadata;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaDomainRef;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.function.Supplier;

import static com.lilamaris.capstone.shared.domain.util.Validation.requireField;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "membership",
        indexes = {
                @Index(
                        name = "membership_lookup_idx",
                        columnList = "actor_type, actor_id, resource_type, resource_id"
                )
        }
)
@EntityListeners(AuditingEntityListener.class)
public class Membership implements Persistable<MembershipId>, Identifiable<MembershipId>, Auditable {
    @Embedded
    private final JpaAuditMetadata audit = new JpaAuditMetadata();

    @Getter(AccessLevel.NONE)
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private MembershipId id;

    @Embedded
    private JpaActor actor;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "type.name", column = @Column(name = "resource_type")),
            @AttributeOverride(name = "id", column = @Column(name = "resource_id"))
    })
    private JpaDomainRef resource;

    @Setter
    @Enumerated(EnumType.STRING)
    private MembershipStatus status;

    @Transient
    private boolean isNew = true;

    protected Membership(
            MembershipId id,
            JpaActor actor,
            JpaDomainRef resource,
            MembershipStatus status
    ) {
        this.id = requireField(id, "id");
        this.actor = requireField(actor, "actor");
        this.resource = requireField(resource, "resource");
        this.status = requireField(status, "status");
    }

    public static Membership create(
            Supplier<MembershipId> idSupplier,
            CanonicalActor actor,
            DomainRef resourceRef
    ) {
        var act = JpaActor.from(actor);
        var ref = JpaDomainRef.from(resourceRef);
        var membership = new Membership(
                idSupplier.get(),
                act,
                ref,
                MembershipStatus.SUSPENDED
        );
        membership.registerCreated();
        return membership;
    }

    @Override
    public MembershipId id() {
        return id;
    }

    @Override
    public AuditMetadata auditMetadata() {
        return audit;
    }

    private void registerCreated() {
    }

    @Override
    public MembershipId getId() {
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
