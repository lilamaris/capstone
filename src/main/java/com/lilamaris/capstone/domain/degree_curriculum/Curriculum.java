package com.lilamaris.capstone.domain.degree_curriculum;

import com.lilamaris.capstone.domain.AbstractUUIDDomainId;
import com.lilamaris.capstone.domain.DomainType;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record Curriculum(
        Id id
) {
    public enum Type implements DomainType {
        INSTANCE;

        @Override
        public String getName() {
            return "curriculum";
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
