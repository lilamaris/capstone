package com.lilamaris.capstone.domain.model.capstone.timeline.id;

import com.fasterxml.jackson.annotation.JsonValue;
import com.lilamaris.capstone.domain.model.common.id.DomainRef;
import com.lilamaris.capstone.domain.model.common.id.impl.DefaultDomainRef;
import com.lilamaris.capstone.domain.model.common.id.impl.DefaultUuidDomainId;
import com.lilamaris.capstone.domain.model.common.mixin.Referenceable;
import com.lilamaris.capstone.domain.model.common.type.CoreDomainType;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimelineId extends DefaultUuidDomainId implements Referenceable {
    @JsonValue
    protected UUID value;

    public TimelineId(UUID value) {
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
        return DefaultDomainRef.from(CoreDomainType.TIMELINE, this);
    }
}
