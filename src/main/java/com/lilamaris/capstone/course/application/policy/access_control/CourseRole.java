package com.lilamaris.capstone.course.application.policy.access_control;

import com.lilamaris.capstone.shared.application.access_control.contract.DomainRole;

public enum CourseRole implements DomainRole {
    VIEWER,
    MAINTAINER
}
