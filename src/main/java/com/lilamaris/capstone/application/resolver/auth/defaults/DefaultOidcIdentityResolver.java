package com.lilamaris.capstone.application.resolver.auth.defaults;

import com.lilamaris.capstone.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.application.policy.identity.IdGenerationContext;
import com.lilamaris.capstone.application.port.out.AccountPort;
import com.lilamaris.capstone.application.port.out.UserPort;
import com.lilamaris.capstone.application.resolver.auth.AuthIdentity;
import com.lilamaris.capstone.application.resolver.auth.OidcIdentityResolver;
import com.lilamaris.capstone.domain.model.auth.account.Account;
import com.lilamaris.capstone.domain.model.auth.account.Provider;
import com.lilamaris.capstone.domain.model.auth.account.id.AccountId;
import com.lilamaris.capstone.domain.model.capstone.user.Role;
import com.lilamaris.capstone.domain.model.capstone.user.User;
import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultOidcIdentityResolver implements OidcIdentityResolver {
    private final AccountPort accountPort;
    private final UserPort userPort;

    private final IdGenerationContext ids;

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
            user = User.create(displayName, Role.USER, ids.next(UserId.class));
            account = Account.create(user.id(), provider, providerId, email, displayName, ids.next(AccountId.class));
            user = userPort.save(user);
            account = accountPort.save(account);
            isCreated = true;
        }

        return new AuthIdentity(user, account, isCreated);
    }
}
