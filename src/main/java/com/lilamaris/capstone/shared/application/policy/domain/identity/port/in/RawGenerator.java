package com.lilamaris.capstone.shared.application.policy.domain.identity.port.in;

@FunctionalInterface
public interface RawGenerator<R> {
    R generate();
}
