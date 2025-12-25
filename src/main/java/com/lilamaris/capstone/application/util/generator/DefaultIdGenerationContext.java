package com.lilamaris.capstone.application.util.generator;

import com.lilamaris.capstone.domain.model.common.id.*;

import java.util.Map;

public class DefaultIdGenerationContext implements IdGenerationContext {
    public record Binding<T extends DomainId<R>, R>(IdSpec<T, R> spec, RawGenerator<R> rawGen) {}

    private final Map<Class<?>, Binding<?, ?>> bindings;

    public DefaultIdGenerationContext(Map<Class<?>, Binding<?, ?>> bindings) {
        this.bindings = Map.copyOf(bindings);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends DomainId<R>, R> T next(Class<T> clazz) {
        var binding = (Binding<T, R>) bindings.get(clazz);
        if (binding == null) {
            throw new IllegalStateException("No binding registered for class: " + clazz.getName());
        }

        return binding.spec.fromRaw(binding.rawGen.generate());
    }

    public static <T extends DomainId<R>, R> Binding<T, R> bind(IdSpec<T, R> spec, RawGenerator<R> rawGen) {
        return new Binding<>(spec, rawGen);
    }
}
