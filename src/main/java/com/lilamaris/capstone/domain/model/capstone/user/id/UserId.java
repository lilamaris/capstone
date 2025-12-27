package com.lilamaris.capstone.domain.model.capstone.user.id;

import com.fasterxml.jackson.annotation.JsonValue;
import com.lilamaris.capstone.domain.model.common.defaults.DefaultDomainRef;
import com.lilamaris.capstone.domain.model.common.defaults.DefaultUuidDomainId;
import com.lilamaris.capstone.domain.model.common.domain.contract.Referenceable;
import com.lilamaris.capstone.domain.model.common.domain.id.DomainRef;
import com.lilamaris.capstone.domain.model.common.domain.id.ExternalizableId;
import com.lilamaris.capstone.domain.model.common.domain.type.CoreDomainType;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserId extends DefaultUuidDomainId implements Referenceable, ExternalizableId {
    @JsonValue
    protected UUID value;

    public UserId(UUID value) {
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
    public DomainRef ref() {
        return new DefaultDomainRef(CoreDomainType.USER, this);
    }

    @Override
    public String asString() {
        return value.toString();
    }
}
