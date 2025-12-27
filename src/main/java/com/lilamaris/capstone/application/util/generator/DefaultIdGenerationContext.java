package com.lilamaris.capstone.application.util.generator;

import com.lilamaris.capstone.domain.model.common.domain.id.DomainId;
import com.lilamaris.capstone.domain.model.common.domain.id.IdGenerationContext;
import com.lilamaris.capstone.domain.model.common.domain.id.IdSpec;
import com.lilamaris.capstone.domain.model.common.domain.id.RawGenerator;

import java.util.HashMap;
import java.util.Map;

public class DefaultIdGenerationContext implements IdGenerationContext {
    private final Map<Class<?>, Binding<?, ?>> bindings;

    public DefaultIdGenerationContext() {
        this.bindings = new HashMap<>();
    }

    public <T extends DomainId<R>, R> void register(Class<T> idClass, IdSpec<T, R> idSpec, RawGenerator<R> rawGenerator) {
        var bind = new Binding<>(idSpec, rawGenerator);
        bindings.put(idClass, bind);
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

    public record Binding<T extends DomainId<R>, R>(IdSpec<T, R> spec, RawGenerator<R> rawGen) {
    }
}
