package com.lilamaris.capstone.shared.application.policy.identity.defaults;

import com.lilamaris.capstone.shared.application.policy.identity.port.in.IdGenerationDirectory;
import com.lilamaris.capstone.shared.application.policy.identity.port.in.IdGenerator;
import com.lilamaris.capstone.shared.domain.id.DomainId;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DefaultIdGenerationDirectory implements IdGenerationDirectory {
    private final Map<Class<? extends DomainId<?>>, IdGenerator<?>> generators;

    public DefaultIdGenerationDirectory(List<IdGenerator<?>> generators) {
        this.generators = generators.stream()
                .collect(Collectors.toUnmodifiableMap(
                        IdGenerator::supports,
                        Function.identity()
                ));
    }

    @Override
    public <T extends DomainId<?>> Supplier<T> next(Class<T> type) {
        @SuppressWarnings("unchecked")
        var generator = (IdGenerator<T>) generators.get(type);
        if (generator == null) {
            throw new IllegalStateException("No generator for: " + type.getName());
        }

        return generator::next;
    }
}
