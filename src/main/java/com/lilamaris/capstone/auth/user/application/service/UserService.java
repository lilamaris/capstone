package com.lilamaris.capstone.auth.user.application.service;

import com.lilamaris.capstone.auth.scenario.auth.application.port.out.AuthUserEntry;
import com.lilamaris.capstone.auth.scenario.auth.application.port.out.AuthUserRegistrar;
import com.lilamaris.capstone.auth.scenario.auth.application.port.out.AuthUserResolver;
import com.lilamaris.capstone.auth.user.application.port.in.UserExistenceChecker;
import com.lilamaris.capstone.auth.user.application.port.out.UserStore;
import com.lilamaris.capstone.auth.user.domain.User;
import com.lilamaris.capstone.auth.user.domain.id.UserId;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.DomainRefResolverDirectory;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerationDirectory;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.type.AggregateDomainType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements
        AuthUserRegistrar,
        AuthUserResolver,
        UserExistenceChecker {
    private final UserStore userStore;
    private final IdGenerationDirectory ids;
    private final DomainRefResolverDirectory refDirs;

    @Override
    public Optional<AuthUserEntry> resolve(ExternalizableId externalId) {
        var id = refDirs.resolve(externalId, AggregateDomainType.USER, UserId.class);
        return userStore.getById(id).map(AuthUserEntry::from);
    }

    @Override
    public AuthUserEntry register(String displayName) {
        var user = User.create(
                ids.next(UserId.class),
                displayName
        );

        var created = userStore.save(user);

        return AuthUserEntry.from(created);
    }

    @Override
    public boolean isExist(ExternalizableId id) {
        var userId = refDirs.resolve(id, AggregateDomainType.USER, UserId.class);
        return userStore.isExists(userId);
    }
}
