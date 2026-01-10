package com.lilamaris.capstone.timeline.application.policy.privilege;

import com.lilamaris.capstone.shared.application.policy.domain.role.port.in.DomainRole;

public enum TimelineRole implements DomainRole {
    MEMBER,
    CONTRIBUTOR,
    MAINTAINER
}
