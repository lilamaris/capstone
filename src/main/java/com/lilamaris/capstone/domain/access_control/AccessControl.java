package com.lilamaris.capstone.domain.access_control;

import com.lilamaris.capstone.domain.AbstractUUIDDomainId;
import com.lilamaris.capstone.domain.DomainType;
import com.lilamaris.capstone.domain.embed.Audit;
import com.lilamaris.capstone.domain.embed.DomainRef;
import com.lilamaris.capstone.domain.exception.DomainIllegalArgumentException;
import com.lilamaris.capstone.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.Optional;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE, toBuilder = true)
public record AccessControl(
        Id id,
        User.Id userId,
        DomainRef resource,
        String scopedRole,
        Audit audit
) {
    public AccessControl {
        if (userId == null) throw new DomainIllegalArgumentException("Field 'userId' must not be null.'");
        if (resource == null) throw new DomainIllegalArgumentException("Field 'resource' must not be null.");
        if (scopedRole == null) throw new DomainIllegalArgumentException("Field 'scopedRole' must not be null.");

        id = Optional.ofNullable(id).orElseGet(Id::new);
    }

    public static AccessControl create(User.Id userId, DomainRef resource, String scopedRole) {
        return AccessControl.builder().userId(userId).resource(resource).scopedRole(scopedRole).build();
    }

    public AccessControl regrant(String scopedRole) {
        return toBuilder().scopedRole(scopedRole).build();
    }

    public enum Type implements DomainType {
        INSTANCE;

        @Override
        public String getName() {
            return "access-control";
        }
    }

    public static class Id extends AbstractUUIDDomainId<Type> {
        public Id(UUID value) {
            super(value);
        }

        public Id() {
            super();
        }

        @Override
        public Type getDomainType() {
            return Type.INSTANCE;
        }
    }
}
