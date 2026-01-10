package com.lilamaris.capstone.course.application.policy.access_control;

import com.lilamaris.capstone.shared.application.policy.role.port.in.ResourceRole;

public enum CourseRole implements ResourceRole {
    VIEWER,
    MAINTAINER
}
