package com.lilamaris.capstone.course.application.policy.access_control;

import com.lilamaris.capstone.shared.application.policy.domain.role.port.in.DomainRole;

public enum CourseRole implements DomainRole {
    VIEWER,
    MAINTAINER
}
