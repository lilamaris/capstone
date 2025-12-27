package com.lilamaris.capstone.application.util.generator;

import com.lilamaris.capstone.domain.model.common.domain.id.RawGenerator;

import java.util.UUID;

public class UuidRawGenerator implements RawGenerator<UUID> {
    @Override
    public UUID generate() {
        return UUID.randomUUID();
    }
}
