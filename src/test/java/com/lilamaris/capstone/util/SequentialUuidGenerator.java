package com.lilamaris.capstone.util;

import com.lilamaris.capstone.domain.model.common.domain.id.RawGenerator;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class SequentialUuidGenerator implements RawGenerator<UUID> {
    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public UUID generate() {
        long lsb = counter.getAndIncrement();
        return new UUID(0L, lsb);
    }
}
