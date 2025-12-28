package com.lilamaris.capstone.application.config.access_control.privilege.timeline;

import com.lilamaris.capstone.application.policy.access_control.DomainRole;

public enum TimelineRole implements DomainRole {
    VIEWER,
    CONTRIBUTOR,
    MAINTAINER
}
