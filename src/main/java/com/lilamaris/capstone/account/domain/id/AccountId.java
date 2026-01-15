package com.lilamaris.capstone.account.domain.id;

import com.fasterxml.jackson.annotation.JsonValue;
import com.lilamaris.capstone.shared.domain.defaults.DefaultUuidDomainId;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountId extends DefaultUuidDomainId implements ExternalizableId {
    @JsonValue
    protected UUID value;

    public AccountId(UUID value) {
        super(value);
    }

    @Override
    public UUID value() {
        return value;
    }

    @Override
    protected void init(UUID value) {
        this.value = value;
    }

    @Override
    public String asString() {
        return value.toString();
    }
}
