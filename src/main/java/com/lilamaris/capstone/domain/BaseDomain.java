package com.lilamaris.capstone.domain;

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

    default boolean sameIdentityAs(SELF other) {
        return other != null && id().equals(other.id());
    }

    default String getDomainName() {
        return this.getClass().getSimpleName();
    }
}
