package com.lilamaris.capstone.util;

import com.lilamaris.capstone.domain.model.common.id.DomainId;
import com.lilamaris.capstone.domain.model.common.id.IdGenerationContext;
import com.lilamaris.capstone.domain.model.common.id.IdSpec;

import java.util.Map;

public class FixedIdGenerationContext implements IdGenerationContext {
    private final Map<IdSpec<?, ?>, Object> fixed;

    public FixedIdGenerationContext(Map<IdSpec<?, ?>, Object> fixed) {
        this.fixed = fixed;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends DomainId<R>, R> T next(IdSpec<T, R> spec) {
        R raw = (R) fixed.get(spec);
        return spec.fromRaw(raw);
    }
}
