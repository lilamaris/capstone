package com.lilamaris.capstone.domain.model.capstone.timeline.policy;

import com.lilamaris.capstone.domain.model.common.domain.policy.DomainAction;

public enum TimelineAction implements DomainAction {
    READ,
    UPDATE_METADATA,
    MIGRATE,
    MERGE,
}
