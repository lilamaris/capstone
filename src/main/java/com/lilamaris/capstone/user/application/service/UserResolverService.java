package com.lilamaris.capstone.user.application.service;

import com.lilamaris.capstone.scenario.auth.application.port.out.AuthUserEntry;
import com.lilamaris.capstone.scenario.auth.application.port.out.AuthUserResolver;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.DomainRefResolverDirectory;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.type.AggregateDomainType;
import com.lilamaris.capstone.user.application.port.out.UserPort;
import com.lilamaris.capstone.user.domain.id.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserResolverService implements
        AuthUserResolver {
    private final UserPort userPort;
    private final DomainRefResolverDirectory refs;

    @Override
    public Optional<AuthUserEntry> resolve(ExternalizableId externalId) {
        var id = refs.resolve(externalId, AggregateDomainType.USER, UserId.class);
        return userPort.getById(id).map(AuthUserEntry::from);
    }
}
