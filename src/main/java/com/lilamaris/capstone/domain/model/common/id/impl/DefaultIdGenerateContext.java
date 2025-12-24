package com.lilamaris.capstone.domain.model.common.id.impl;

import com.lilamaris.capstone.domain.model.common.id.*;

import java.util.Map;
import java.util.Objects;

public class DefaultIdGenerateContext implements IdGenerationContext {
    private final IdGenerator idGenerator;
    private final Map<IdSpec<?, ?>, RawGenerator<?>> rawGenerators;

    public DefaultIdGenerateContext(IdGenerator idGenerator, Map<IdSpec<?, ?>, RawGenerator<?>> rawGenerators) {
        this.idGenerator = Objects.requireNonNull(idGenerator);
        this.rawGenerators = Map.copyOf(rawGenerators);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends DomainId<R>, R> T next(IdSpec<T, R> spec) {
        var rawGen = (RawGenerator<R>) rawGenerators.get(spec);

        if (rawGen == null) {
            throw new IllegalStateException(
                    "No RawGenerator registered for IdSpec: " + spec
            );
        }

        return idGenerator.generate(spec, rawGen);
    }
}
