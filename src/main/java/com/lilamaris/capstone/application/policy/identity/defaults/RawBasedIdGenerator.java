package com.lilamaris.capstone.application.policy.identity.defaults;

import com.lilamaris.capstone.application.policy.identity.IdGenerator;
import com.lilamaris.capstone.application.policy.identity.IdSpec;
import com.lilamaris.capstone.application.policy.identity.RawGenerator;
import com.lilamaris.capstone.domain.model.common.domain.id.DomainId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class RawBasedIdGenerator<T extends DomainId<R>, R> implements IdGenerator<T> {
    private final Class<T> type;
    private final IdSpec<T, R> spec;
    private final RawGenerator<R> raw;

    @Override
    public T next() {
        return spec.fromRaw(raw.generate());
    }

    @Override
    public Class<T> supports() {
        return type;
    }
}
