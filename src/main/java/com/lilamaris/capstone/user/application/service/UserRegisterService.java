package com.lilamaris.capstone.user.application.service;

import com.lilamaris.capstone.scenario.auth.application.port.out.AuthUserRegistrar;
import com.lilamaris.capstone.scenario.auth.application.port.out.UserEntry;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerationDirectory;
import com.lilamaris.capstone.user.application.port.out.UserPort;
import com.lilamaris.capstone.user.domain.User;
import com.lilamaris.capstone.user.domain.id.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRegisterService implements
        AuthUserRegistrar {
    private final UserPort userPort;
    private final IdGenerationDirectory ids;

    @Override
    public UserEntry register(String displayName) {
        var user = User.create(
                ids.next(UserId.class),
                displayName
        );

        var created = userPort.save(user);

        return UserEntry.from(created);
    }
}
