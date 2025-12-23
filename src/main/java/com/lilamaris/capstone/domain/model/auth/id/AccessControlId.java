package com.lilamaris.capstone.domain.model.auth.id;

import com.lilamaris.capstone.domain.model.common.impl.jpa.JpaDefaultUuidDomainId;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessControlId extends JpaDefaultUuidDomainId {
    public AccessControlId(UUID value) {
        super(value);
    }

    public static AccessControlId newId() {
        return new AccessControlId(UUID.randomUUID());
    }
}
