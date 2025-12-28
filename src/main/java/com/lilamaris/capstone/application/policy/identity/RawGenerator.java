package com.lilamaris.capstone.application.policy.identity;

@FunctionalInterface
public interface RawGenerator<R> {
    R generate();
}
