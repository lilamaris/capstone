package com.lilamaris.capstone.membership.domain;

import com.lilamaris.capstone.membership.domain.id.MembershipId;
import com.lilamaris.capstone.shared.domain.contract.Auditable;
import com.lilamaris.capstone.shared.domain.contract.Identifiable;
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

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "membership")
@EntityListeners(AuditingEntityListener.class)
public class Membership implements Identifiable<MembershipId>, Auditable {
    @Embedded
    private final JpaAuditMetadata audit = new JpaAuditMetadata();

    @Getter(AccessLevel.NONE)
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private MembershipId id;

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

    @Enumerated(EnumType.STRING)
    private MembershipStatus status;

    @Enumerated(EnumType.STRING)
    private MembershipVisibility visibility;

    @Override
    public MembershipId id() {
        return id;
    }

    @Override
    public AuditMetadata auditMetadata() {
        return audit;
    }
}
