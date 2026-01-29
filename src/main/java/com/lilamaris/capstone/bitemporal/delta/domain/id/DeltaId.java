package com.lilamaris.capstone.bitemporal.delta.domain.id;

import com.fasterxml.jackson.annotation.JsonValue;
import com.lilamaris.capstone.shared.domain.contract.CanonicalIdentity;
import com.lilamaris.capstone.shared.domain.contract.Referenceable;
import com.lilamaris.capstone.shared.domain.defaults.DefaultDomainRef;
import com.lilamaris.capstone.shared.domain.defaults.DefaultExternalizableId;
import com.lilamaris.capstone.shared.domain.defaults.DefaultUuidDomainId;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.type.AggregateDomainType;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeltaId extends DefaultUuidDomainId implements Referenceable, CanonicalIdentity {
    @JsonValue
    protected UUID value;

    public DeltaId(UUID value) {
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
        return new DefaultDomainRef(AggregateDomainType.DELTA, externalId());
    }

    @Override
    public ExternalizableId externalId() {
        return DefaultExternalizableId.from(value.toString());
    }
}
