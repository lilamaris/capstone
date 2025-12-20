package com.lilamaris.capstone.application.port.out;

import com.lilamaris.capstone.domain.access_control.AccessControl;
import com.lilamaris.capstone.domain.embed.DomainRef;
import com.lilamaris.capstone.domain.user.User;

import java.util.Optional;

public interface AccessControlPort {
    boolean hasGrant(User.Id userId, DomainRef domainRef, String scopedRole);
    Optional<AccessControl> getById(AccessControl.Id id);

    AccessControl save(AccessControl domain);
    void delete(AccessControl.Id id);
}
