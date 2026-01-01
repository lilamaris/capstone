package com.lilamaris.capstone.shared.application.config.access_control.privilege.timeline;

import com.lilamaris.capstone.access_control.application.port.in.DomainRole;

public enum TimelineRole implements DomainRole {
    VIEWER,
    CONTRIBUTOR,
    MAINTAINER
}
