package com.lilamaris.capstone.shared.application.policy.identity.port.in;

@FunctionalInterface
public interface RawParser<R> {
    R parse(String raw);
}
