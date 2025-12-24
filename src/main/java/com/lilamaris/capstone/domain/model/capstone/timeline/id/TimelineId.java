package com.lilamaris.capstone.domain.model.capstone.timeline.id;

import com.fasterxml.jackson.annotation.JsonValue;
import com.lilamaris.capstone.domain.model.capstone.timeline.spec.TimelineIdSpec;
import com.lilamaris.capstone.domain.model.common.id.impl.DefaultUuidDomainId;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimelineId extends DefaultUuidDomainId {
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

    public static final TimelineIdSpec SPEC = new TimelineIdSpec();
}
