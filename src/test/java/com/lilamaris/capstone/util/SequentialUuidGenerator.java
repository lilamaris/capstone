package com.lilamaris.capstone.util;


import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.RawGenerator;

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
