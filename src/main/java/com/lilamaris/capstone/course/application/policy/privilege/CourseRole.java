package com.lilamaris.capstone.course.application.policy.privilege;

import com.lilamaris.capstone.shared.application.policy.domain.role.port.in.DomainRole;

public enum CourseRole implements DomainRole {
    VIEWER,
    MAINTAINER
}
