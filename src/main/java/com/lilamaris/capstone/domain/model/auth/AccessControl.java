package com.lilamaris.capstone.domain.model.auth;

import com.lilamaris.capstone.domain.model.auth.id.AccessControlId;
import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;
import com.lilamaris.capstone.domain.model.common.CoreDomainType;
import com.lilamaris.capstone.domain.model.common.DomainId;
import com.lilamaris.capstone.domain.model.common.DomainRef;
import com.lilamaris.capstone.domain.model.common.impl.jpa.JpaDefaultAuditableDomain;
import com.lilamaris.capstone.domain.model.common.impl.jpa.JpaDomainRef;
import com.lilamaris.capstone.domain.model.common.mixin.Identifiable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static com.lilamaris.capstone.domain.model.util.Validation.requireField;

@Getter
@ToString
@Entity
@Table(name = "_access_control")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessControl extends JpaDefaultAuditableDomain implements Identifiable<AccessControlId> {
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

    private String scopedRole;

    private AccessControl(AccessControlId id, UserId userId, JpaDomainRef resourceRef, String scopedRole) {
        this.id = requireField(id, "id");
        this.userId = requireField(userId, "userId");
        this.resourceRef = requireField(resourceRef, "resourceRef");
        this.scopedRole = requireField(scopedRole, "scopedRole");
    }

    public static AccessControl create(UserId userId, CoreDomainType resourceType, DomainId<?> resourceId, String scopedRole) {
        var ref = JpaDomainRef.from(resourceType, resourceId);
        return new AccessControl(AccessControlId.newId(), userId, ref, scopedRole);
    }

    public static AccessControl create(UserId userId, DomainRef resourceRef, String scopedRole) {
        var ref = JpaDomainRef.from(resourceRef);
        return new AccessControl(AccessControlId.newId(), userId, ref, scopedRole);
    }

    @Override
    public AccessControlId id() {
        return id;
    }
}
