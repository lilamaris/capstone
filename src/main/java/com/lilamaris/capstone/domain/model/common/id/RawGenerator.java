package com.lilamaris.capstone.domain.model.common.id;

@FunctionalInterface
public interface RawGenerator<R> {
    R generate();
}
