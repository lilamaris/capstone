package com.lilamaris.capstone.domain.access_control;

import com.lilamaris.capstone.domain.AbstractUUIDDomainId;
import com.lilamaris.capstone.domain.DomainId;
import com.lilamaris.capstone.domain.DomainType;
import com.lilamaris.capstone.domain.user.User;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record AccessControl (
        Id id,
        DomainId<?, ?> resourceId,
        User.Id userId,
        String role
) {
    public enum Type implements DomainType {
        INSTANCE;

        @Override
        public String getName() {
            return "course-offer";
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
