package com.lilamaris.capstone.application.util.auth;

import com.lilamaris.capstone.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.application.port.out.AuthPort;
import com.lilamaris.capstone.application.port.out.UserPort;
import com.lilamaris.capstone.domain.user.Account;
import com.lilamaris.capstone.domain.user.Provider;
import com.lilamaris.capstone.domain.user.Role;
import com.lilamaris.capstone.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultOidcIdentityResolver implements OidcIdentityResolver {
    private final AuthPort authPort;
    private final UserPort userPort;

    @Override
    public AuthIdentity resolve(Provider provider, String providerId, String email, String displayName) {
        User user;
        Account account;
        boolean isCreated = false;

        var accountOptional = authPort.getBy(provider, providerId);

        if (accountOptional.isPresent()) {
            account = accountOptional.get();
            user = userPort.getById(account.userId()).orElseThrow(() -> new ResourceNotFoundException(
                    String.format("User with id '%s' not found.", account.userId().getValue())
            ));
        } else {
            account = Account.createOidc(provider, providerId, email, displayName);
            user = User.create(displayName, Role.USER);
            user = user.linkAccount(account);
            user = userPort.save(user);
            isCreated = true;
        }

        return new AuthIdentity(user, account, isCreated);
    }
}
