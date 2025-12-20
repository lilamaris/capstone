package com.lilamaris.capstone.application;

import com.lilamaris.capstone.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.application.port.in.AccessControlUseCase;
import com.lilamaris.capstone.application.port.in.result.AccessControlResult;
import com.lilamaris.capstone.application.port.out.AccessControlPort;
import com.lilamaris.capstone.domain.DomainId;
import com.lilamaris.capstone.domain.access_control.AccessControl;
import com.lilamaris.capstone.domain.embed.DomainRef;
import com.lilamaris.capstone.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessControlCommandService implements AccessControlUseCase {
    private final AccessControlPort accessControlPort;

    @Override
    public AccessControlResult.Command grant(User.Id userId, DomainId<?, ?> domainId, String scopedRole) {
        var domainRef = DomainRef.from(domainId);
        var domain = AccessControl.create(userId, domainRef, scopedRole);
        var saved = accessControlPort.save(domain);

        return AccessControlResult.Command.from(saved);
    }

    @Override
    public AccessControlResult.Command regrant(AccessControl.Id id, String scopedRole) {
        var domain = accessControlPort.getById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("AccessControl with id '%s' not found.", id.getValue())
        ));

        var updated = domain.regrant(scopedRole);
        var saved = accessControlPort.save(updated);

        return AccessControlResult.Command.from(saved);
    }

    @Override
    public void revoke(AccessControl.Id id) {
        accessControlPort.delete(id);
    }
}
