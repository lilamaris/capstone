package com.lilamaris.capstone.shared.application.identity.contract;

@FunctionalInterface
public interface RawGenerator<R> {
    R generate();
}
