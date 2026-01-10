package com.lilamaris.capstone.shared.application.policy.identity.port.in;

@FunctionalInterface
public interface RawGenerator<R> {
    R generate();
}
