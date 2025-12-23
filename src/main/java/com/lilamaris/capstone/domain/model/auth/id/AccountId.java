package com.lilamaris.capstone.domain.model.auth.id;

import com.lilamaris.capstone.domain.model.common.impl.jpa.JpaDefaultUuidDomainId;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountId extends JpaDefaultUuidDomainId {
    public AccountId(UUID value) {
        super(value);
    }

    public static AccountId newId() {
        return new AccountId(UUID.randomUUID());
    }
}
