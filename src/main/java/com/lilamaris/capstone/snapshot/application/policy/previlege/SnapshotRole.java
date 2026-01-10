package com.lilamaris.capstone.snapshot.application.policy.previlege;

import com.lilamaris.capstone.shared.application.policy.domain.role.port.in.DomainRole;

public enum SnapshotRole implements DomainRole {
    MEMBER,
    CONTRIBUTOR,
    MAINTAINER
}
