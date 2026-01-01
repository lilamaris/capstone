package com.lilamaris.capstone.shared.application.config.access_control.privilege.course;

import com.lilamaris.capstone.access_control.application.port.in.DomainRole;

public enum CourseRole implements DomainRole {
    VIEWER,
    MAINTAINER
}
