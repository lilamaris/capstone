package com.lilamaris.capstone.identity.auth.application.role.internal;

import com.lilamaris.capstone.identity.auth.application.role.port.out.UserGrantedRoleStore;
import com.lilamaris.capstone.identity.auth.domain.role.UserGrantedRole;
import com.lilamaris.capstone.identity.core.role.InitialUserGrantedRoleRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InitialUserGrantedRoleProvisioner {
    private final InitialUserGrantedRoleRegistry registry;
    private final UserGrantedRoleStore store;
    private final Clock clock;

    @Transactional
    public List<UserGrantedRole> grant(UUID userId) {
        var now = clock.instant();

        var initialGrant = registry.getAll().stream()
                .map(nr -> UserGrantedRole.of(
                        userId,
                        nr.namespace(),
                        nr.role(),
                        now
                ))
                .toList();

        store.saveAll(initialGrant);

        return initialGrant;
    }
}
