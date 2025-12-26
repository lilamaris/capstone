package com.lilamaris.capstone.domain.model.auth.access_control;

import com.lilamaris.capstone.domain.model.auth.access_control.id.AccessControlId;
import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;
import com.lilamaris.capstone.domain.model.common.embed.impl.jpa.JpaAuditMetadata;
import com.lilamaris.capstone.domain.model.common.event.AggregateEvent;
import com.lilamaris.capstone.domain.model.common.id.DomainRef;
import com.lilamaris.capstone.domain.model.common.id.impl.JpaDomainRef;
import com.lilamaris.capstone.domain.model.common.mixin.Identifiable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

import static com.lilamaris.capstone.domain.model.util.Validation.requireField;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "_access_control")
@EntityListeners(AuditingEntityListener.class)
public class AccessControl implements Identifiable<AccessControlId> {
    @Getter(AccessLevel.NONE)
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private AccessControlId id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "user_id", insertable = false, updatable = false))
    private UserId userId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "type", column = @Column(name = "resource_type")),
            @AttributeOverride(name = "id", column = @Column(name = "resource_id"))
    })
    private JpaDomainRef resourceRef;

    @Embedded
    private JpaAuditMetadata audit;

    private String scopedRole;

    @Transient
    private List<AggregateEvent> eventList;

    protected AccessControl(
            AccessControlId id,
            UserId userId,
            JpaDomainRef resourceRef,
            String scopedRole
    ) {
        this.id = requireField(id, "id");
        this.userId = requireField(userId, "userId");
        this.resourceRef = requireField(resourceRef, "resourceRef");
        this.scopedRole = requireField(scopedRole, "scopedRole");
    }

    protected static AccessControl create(
            AccessControlId id,
            UserId userId,
            DomainRef resourceRef,
            String scopedRole
    ) {
        var ref = JpaDomainRef.from(resourceRef);
        var accessControl = new AccessControl(id, userId, ref, scopedRole);
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
