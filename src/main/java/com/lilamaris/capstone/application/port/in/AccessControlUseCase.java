package com.lilamaris.capstone.application.port.in;

import com.lilamaris.capstone.application.port.in.result.AccessControlResult;
import com.lilamaris.capstone.domain.DomainId;
import com.lilamaris.capstone.domain.access_control.AccessControl;
import com.lilamaris.capstone.domain.user.User;

public interface AccessControlUseCase {
    AccessControlResult.Command grant(User.Id userId, DomainId<?, ?> domainId, String scopedRole);

    AccessControlResult.Command regrant(AccessControl.Id id, String scopedRole);

    void revoke(AccessControl.Id id);
}
