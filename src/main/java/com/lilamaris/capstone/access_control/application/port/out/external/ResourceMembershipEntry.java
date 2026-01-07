package com.lilamaris.capstone.access_control.application.port.out.external;

import com.lilamaris.capstone.shared.domain.id.DomainRef;

public record ResourceMembershipEntry(
        DomainRef ref,
        String scopedRole
) {
}
