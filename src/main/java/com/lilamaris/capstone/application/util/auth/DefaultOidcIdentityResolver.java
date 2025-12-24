package com.lilamaris.capstone.application.util.auth;

import com.lilamaris.capstone.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.application.port.out.AccountPort;
import com.lilamaris.capstone.application.port.out.UserPort;
import com.lilamaris.capstone.domain.model.auth.account.Account;
import com.lilamaris.capstone.domain.model.auth.account.AccountFactory;
import com.lilamaris.capstone.domain.model.auth.account.Provider;
import com.lilamaris.capstone.domain.model.capstone.user.Role;
import com.lilamaris.capstone.domain.model.capstone.user.User;
import com.lilamaris.capstone.domain.model.capstone.user.UserFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultOidcIdentityResolver implements OidcIdentityResolver {
    private final AccountPort accountPort;
    private final UserPort userPort;

    private final UserFactory userFactory;
    private final AccountFactory accountFactory;

    @Override
    public AuthIdentity resolve(Provider provider, String providerId, String email, String displayName) {
        User user;
        Account account;
        boolean isCreated = false;

        var accountOptional = accountPort.getBy(provider, providerId);

        if (accountOptional.isPresent()) {
            account = accountOptional.get();
            var userId = account.getUserId();
            user = userPort.getById(userId).orElseThrow(() -> new ResourceNotFoundException(
                    String.format("User with id '%s' not found.", userId)
            ));
        } else {
            user = userFactory.create(displayName, Role.USER);
            account = accountFactory.create(user.id(), provider, providerId, email, displayName);
            user = userPort.save(user);
            account = accountPort.save(account);
            isCreated = true;
        }

        return new AuthIdentity(user, account, isCreated);
    }
}
