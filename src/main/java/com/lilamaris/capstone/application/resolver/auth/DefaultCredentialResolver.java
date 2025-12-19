package com.lilamaris.capstone.application.resolver.auth;

import com.lilamaris.capstone.application.exception.ApplicationInvariantException;
import com.lilamaris.capstone.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.application.port.out.AuthPort;
import com.lilamaris.capstone.application.port.out.UserPort;
import com.lilamaris.capstone.domain.user.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DefaultCredentialResolver implements CredentialIdentityResolver {
    private final AuthPort authPort;
    private final UserPort userPort;

    @Override
    public AuthIdentity resolve(String email, Function<String, Boolean> challengeFunction) {
        var account = authPort.getBy(Provider.LOCAL, email).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Account with email '%s' not found.", email)
        ));

        if (!challengeFunction.apply(account.passwordHash())) {
            throw new ApplicationInvariantException("RESOLVER_FAILURE", "Challenge failed.");
        }

        var userId = account.userId();
        var user = userPort.getById(userId).orElseThrow(() -> new ResourceNotFoundException(
                String.format("User with id '%s' not found.", userId.getValue())
        ));

        return new AuthIdentity(user, account, false);
    }
}
