package com.lilamaris.capstone.domain.model.common.domain.id;

@FunctionalInterface
public interface RawGenerator<R> {
    R generate();
}
