package com.lilamaris.capstone.application.util.auth;

import com.lilamaris.capstone.application.exception.ApplicationInvariantException;
import com.lilamaris.capstone.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.application.port.out.AccountPort;
import com.lilamaris.capstone.application.port.out.UserPort;
import com.lilamaris.capstone.domain.model.auth.account.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class DefaultCredentialResolver implements CredentialIdentityResolver {
    private final AccountPort accountPort;
    private final UserPort userPort;

    @Override
    public AuthIdentity resolve(String email, Function<String, Boolean> challengeFunction) {
        var account = accountPort.getBy(Provider.LOCAL, email).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Account with email '%s' not found.", email)
        ));

        if (!challengeFunction.apply(account.getPasswordHash())) {
            throw new ApplicationInvariantException("RESOLVER_FAILURE", "Challenge failed.");
        }

        var userId = account.getUserId();
        var user = userPort.getById(userId).orElseThrow(() -> new ResourceNotFoundException(
                String.format("User with id '%s' not found.", userId)
        ));

        return new AuthIdentity(user, account, false);
    }
}
