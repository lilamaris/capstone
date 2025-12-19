package com.lilamaris.capstone.domain.auth;

import com.lilamaris.capstone.domain.AbstractStringDomainId;
import com.lilamaris.capstone.domain.DomainType;
import com.lilamaris.capstone.domain.user.User;
import lombok.Builder;

@Builder(toBuilder = true)
public record RefreshToken(
        Id id,
        User.Id userId
) {
    public enum Type implements DomainType {
        INSTANCE;

        @Override
        public String getName() {
            return "refresh-token";
        }
    }

    public static class Id extends AbstractStringDomainId<Type> {
        public Id(String value) {
            super(value);
        }

        @Override
        public Type getDomainType() {
            return Type.INSTANCE;
        }
    }
}
