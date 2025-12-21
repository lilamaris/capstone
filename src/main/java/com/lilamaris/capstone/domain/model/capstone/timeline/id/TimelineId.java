package com.lilamaris.capstone.domain.model.capstone.timeline.id;

import com.lilamaris.capstone.domain.common.impl.DefaultUuidDomainId;
import jakarta.persistence.Embeddable;
import lombok.ToString;

import java.util.UUID;

@ToString(callSuper = true)
@Embeddable
public class TimelineId extends DefaultUuidDomainId {
    public TimelineId() {
        super();
    }

    public TimelineId(UUID value) {
        super(value);
    }

    public static TimelineId newId() {
        return new TimelineId();
    }
}
