package com.lilamaris.capstone.domain.model.capstone.user;

import com.lilamaris.capstone.domain.exception.DomainIllegalArgumentException;
import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;
import com.lilamaris.capstone.domain.model.common.persistence.jpa.JpaAuditMetadata;
import com.lilamaris.capstone.domain.model.common.domain.id.DomainRef;
import com.lilamaris.capstone.domain.model.common.defaults.DefaultDomainRef;
import com.lilamaris.capstone.domain.model.common.domain.contract.Identifiable;
import com.lilamaris.capstone.domain.model.common.domain.contract.Referenceable;
import com.lilamaris.capstone.domain.model.common.domain.type.CoreDomainType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.function.Supplier;

@Getter
@ToString
@Entity
@Table(name = "usr")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements Identifiable<UserId>, Referenceable {
    @Embedded
    private final JpaAuditMetadata audit = new JpaAuditMetadata();
    @Getter(AccessLevel.NONE)
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private UserId id;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String displayName;

    protected User(UserId id, String displayName, Role role) {
        if (id == null) throw new DomainIllegalArgumentException("Field 'id' must not be null.");
        if (displayName == null) throw new DomainIllegalArgumentException("Field 'displayName' must not be null.");
        if (role == null) throw new DomainIllegalArgumentException("Field 'role' must not be null.");

        this.id = id;
        this.displayName = displayName;
        this.role = role;
    }

    public static User create(String displayName, Role role, Supplier<UserId> idSupplier) {
        var user = new User(idSupplier.get(), displayName, role);
        user.registerCreated();
        return user;
    }

    private void registerCreated() {

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
