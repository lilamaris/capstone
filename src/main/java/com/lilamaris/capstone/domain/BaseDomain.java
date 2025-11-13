package com.lilamaris.capstone.domain;

import java.util.UUID;

public interface BaseDomain<
        I extends BaseDomain.Id<?>,
        SELF extends BaseDomain<I, SELF>
> {
    I id();

    interface Id<I> {
        I value();
        default String asString() { return value().toString(); }
        default String getDomainName() {
            var enclosing = this.getClass().getEnclosingClass();
            return enclosing != null ? enclosing.getSimpleName() : this.getClass().getSimpleName();
        }
    }

    record UuidId(UUID value) implements Id<UUID> {
        public static UuidId random() { return new UuidId(UUID.randomUUID()); }
        public static UuidId from(UUID value) { return new UuidId(value); }
    }

    default boolean sameIdentityAs(SELF other) {
        return other != null && id().equals(other.id());
    }

    default String getDomainName() {
        return this.getClass().getSimpleName();
    }
}
