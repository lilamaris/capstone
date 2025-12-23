package com.lilamaris.capstone.domain.model.capstone.user;

import com.lilamaris.capstone.domain.exception.DomainIllegalArgumentException;
import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;
import com.lilamaris.capstone.domain.model.common.CoreDomainType;
import com.lilamaris.capstone.domain.model.common.DomainRef;
import com.lilamaris.capstone.domain.model.common.impl.DefaultDomainRef;
import com.lilamaris.capstone.domain.model.common.impl.jpa.JpaDefaultAuditableDomain;
import com.lilamaris.capstone.domain.model.common.mixin.Identifiable;
import com.lilamaris.capstone.domain.model.common.mixin.Referenceable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Entity
@Table(name = "usr")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends JpaDefaultAuditableDomain implements Identifiable<UserId>, Referenceable {
    @Getter(AccessLevel.NONE)
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private UserId id;

    @Enumerated(EnumType.STRING)
    private Role role;
    private String displayName;

    private User(UserId id, String displayName, Role role) {
        if (id == null) throw new DomainIllegalArgumentException("Field 'id' must not be null.");
        if (displayName == null) throw new DomainIllegalArgumentException("Field 'displayName' must not be null.");
        if (role == null) throw new DomainIllegalArgumentException("Field 'role' must not be null.");

        this.id = id;
        this.displayName = displayName;
        this.role = role;
    }

    public static User create(String displayName, Role role) {
        return new User(UserId.newId(), displayName, role);
    }

    @Override
    public UserId id() {
        return id;
    }

    @Override
    public DomainRef ref() {
        return DefaultDomainRef.from(CoreDomainType.USER, id);
    }
}
