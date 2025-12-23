package com.lilamaris.capstone.domain.model.capstone.user.id;

import com.fasterxml.jackson.annotation.JsonValue;
import com.lilamaris.capstone.domain.model.common.impl.DefaultUuidDomainId;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserId extends DefaultUuidDomainId {
    @JsonValue
    protected UUID value;

    public UserId(UUID value) {
        super(value);
    }

    public static UserId newId() {
        return new UserId(newUuid());
    }

    @Override
    public UUID value() {
        return value;
    }

    @Override
    protected void init(UUID value) {
        this.value = value;
    }
}
