package com.lilamaris.capstone.domain.model.capstone.user;

import com.lilamaris.capstone.domain.model.common.impl.jpa.JpaDefaultAuditableDomain;
import com.lilamaris.capstone.domain.model.common.mixin.Identifiable;
import com.lilamaris.capstone.domain.exception.DomainIllegalArgumentException;
import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;
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
public class User extends JpaDefaultAuditableDomain implements Identifiable<UserId> {
    @Getter(AccessLevel.NONE)
    @EmbeddedId
    private UserId id;

    @Enumerated(EnumType.STRING)
    private Role role;
    private String displayName;

    @Override
    public UserId id() {
        return id;
    }

    public static User create(String displayName, Role role) {
        return new User(UserId.newId(), displayName, role);
    }

    private User(UserId id, String displayName, Role role) {
        if (id == null) throw new DomainIllegalArgumentException("Field 'id' must not be null.");
        if (displayName == null) throw new DomainIllegalArgumentException("Field 'displayName' must not be null.");
        if (role == null) throw new DomainIllegalArgumentException("Field 'role' must not be null.");

        this.id = id;
        this.displayName = displayName;
        this.role = role;
    }
}
