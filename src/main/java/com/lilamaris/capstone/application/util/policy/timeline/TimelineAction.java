package com.lilamaris.capstone.application.util.policy.timeline;

import com.lilamaris.capstone.application.util.policy.DomainAction;

public enum TimelineAction implements DomainAction {
    READ,
    UPDATE_METADATA,
    MIGRATE,
    MERGE,
}
