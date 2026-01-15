package com.lilamaris.capstone.user.domain;

import com.lilamaris.capstone.shared.domain.contract.Auditable;
import com.lilamaris.capstone.shared.domain.contract.Identifiable;
import com.lilamaris.capstone.shared.domain.metadata.AuditMetadata;
import com.lilamaris.capstone.shared.domain.persistence.jpa.JpaAuditMetadata;
import com.lilamaris.capstone.user.domain.id.UserId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.function.Supplier;

import static com.lilamaris.capstone.shared.domain.util.Validation.requireField;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "capstone_user")
@EntityListeners(AuditingEntityListener.class)
public class User implements Persistable<UserId>, Identifiable<UserId>, Auditable {
    @Embedded
    private final JpaAuditMetadata audit = new JpaAuditMetadata();

    @Getter(AccessLevel.NONE)
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id", nullable = false, updatable = false))
    private UserId id;

    private String displayName;

    protected User(UserId id, String displayName) {
        this.id = requireField(id, "id");
        this.displayName = requireField(displayName, "displayName");
    }

    public static User create(
            Supplier<UserId> idSupplier,
            String displayName
    ) {
        var user = new User(idSupplier.get(), displayName);
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

    @Transient
    private boolean isNew = true;

    @Override
    public UserId getId() {
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
