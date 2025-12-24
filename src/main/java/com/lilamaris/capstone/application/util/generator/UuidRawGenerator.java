package com.lilamaris.capstone.application.util.generator;

import com.lilamaris.capstone.domain.model.common.id.RawGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidRawGenerator implements RawGenerator<UUID> {
    @Override
    public UUID generate() {
        return UUID.randomUUID();
    }
}
