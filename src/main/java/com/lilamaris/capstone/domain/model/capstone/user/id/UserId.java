package com.lilamaris.capstone.domain.model.capstone.user.id;

import com.lilamaris.capstone.domain.model.common.impl.jpa.JpaDefaultUuidDomainId;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserId extends JpaDefaultUuidDomainId {
    public UserId(UUID value) {
        super(value);
    }

    public static UserId newId() {
        return new UserId(UUID.randomUUID());
    }
}
