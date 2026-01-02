package com.lilamaris.capstone.timeline.application.policy.access_control;

import com.lilamaris.capstone.shared.application.access_control.contract.DomainRole;

public enum TimelineRole implements DomainRole {
    VIEWER,
    CONTRIBUTOR,
    MAINTAINER
}
