package com.lilamaris.capstone.shared.application.access_control.contract;

import com.lilamaris.capstone.shared.domain.id.DomainRef;

public record ResourceMembershipEntry(
        DomainRef ref,
        String scopedRole
) {
}
