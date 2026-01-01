package com.lilamaris.capstone.shared.application.identity;

@FunctionalInterface
public interface RawGenerator<R> {
    R generate();
}
