package com.lilamaris.capstone.shared.application.policy.domain.identity.port.in;

@FunctionalInterface
public interface RawParser<R> {
    R parse(String raw);
}
