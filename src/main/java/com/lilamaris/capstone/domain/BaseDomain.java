package com.lilamaris.capstone.domain;

public sealed interface BaseDomain<
        I extends BaseDomain.Id<?>,
        SELF extends BaseDomain<I, SELF>
> permits Timeline, Snapshot, TransitionLog {
    I id();

    interface Id<I> {
        I value();
        default String asString() { return value().toString(); }
        default String getDomainName() {
            var enclosing = this.getClass().getEnclosingClass();
            return enclosing != null ? enclosing.getSimpleName() : this.getClass().getSimpleName();
        }
    }

    default boolean sameIdentityAs(SELF other) {
        return other != null && id().equals(other.id());
    }

    default String getDomainName() {
        return id().getDomainName();
    }
}
