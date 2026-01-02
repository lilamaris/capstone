package com.lilamaris.capstone.user.domain;

import com.lilamaris.capstone.shared.domain.contract.Auditable;
import com.lilamaris.capstone.shared.domain.contract.Identifiable;
import com.lilamaris.capstone.shared.domain.exception.DomainIllegalArgumentException;
import com.lilamaris.capstone.shared.domain.metadata.AuditMetadata;
import com.lilamaris.capstone.shared.domain.persistence.persistence.jpa.JpaAuditMetadata;
import com.lilamaris.capstone.user.domain.id.UserId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.function.Supplier;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "capstone_user")
@EntityListeners(AuditingEntityListener.class)
public class User implements Identifiable<UserId>, Auditable {
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
    public AuditMetadata auditMetadata() {
        return audit;
    }
}
