package com.lilamaris.capstone.domain.user;

import com.lilamaris.capstone.domain.AbstractUUIDDomainId;
import com.lilamaris.capstone.domain.DomainType;
import com.lilamaris.capstone.domain.embed.Audit;
import com.lilamaris.capstone.domain.exception.DomainIllegalArgumentException;
import lombok.Builder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Builder(toBuilder = true)
public record User(
        Id id,
        String displayName,
        Role role,
        Set<Account> accountSet,
        Audit audit
) {
    public User {
        if (role == null) throw new DomainIllegalArgumentException("Field 'role' must not be null.");
        if (displayName == null) throw new DomainIllegalArgumentException("Field 'displayName' must not be null.");

        id = Optional.ofNullable(id).orElseGet(Id::new);
        accountSet = Optional.ofNullable(accountSet).orElseGet(HashSet::new);

        var curId = id;
        if (!accountSet.stream().allMatch(account -> account.userId().equals(curId))) {
            throw new DomainIllegalArgumentException(
                    "Account does not reference this user Id."
            );
        }
    }

    public static User from(Id id, String displayName, Set<Account> accountSet, Role role, Audit audit) {
        return new User(id, displayName, role, accountSet, audit);
    }

    public static User create(String displayName, Role role) {
        return getDefaultBuilder().displayName(displayName).role(role).build();
    }

    private static UserBuilder getDefaultBuilder() {
        return builder();
    }

    public User linkAccount(Account account) {
        var linkedAccount = account.assignUser(id);
        var newAccountSet = Stream.concat(accountSet.stream(), Stream.of(linkedAccount)).collect(Collectors.toUnmodifiableSet());
        return copyWithAccount(newAccountSet);
    }

    private User copyWithAccount(Set<Account> accountSet) {
        return toBuilder().accountSet(accountSet).build();
    }

    public enum Type implements DomainType {
        INSTANCE;

        @Override
        public String getName() {
            return "user";
        }
    }

    public static class Id extends AbstractUUIDDomainId<Type> {
        public Id() {
            super();
        }

        public Id(UUID uuid) {
            super(uuid);
        }

        @Override
        public Type getDomainType() {
            return Type.INSTANCE;
        }
    }
}
