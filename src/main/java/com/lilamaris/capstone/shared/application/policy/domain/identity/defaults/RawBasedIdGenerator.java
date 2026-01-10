package com.lilamaris.capstone.shared.application.policy.domain.identity.defaults;

import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerator;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdSpec;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.RawGenerator;
import com.lilamaris.capstone.shared.domain.id.DomainId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RawBasedIdGenerator<T extends DomainId<R>, R> implements IdGenerator<T> {
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
