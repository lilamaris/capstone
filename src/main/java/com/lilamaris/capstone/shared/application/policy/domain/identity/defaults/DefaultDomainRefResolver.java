package com.lilamaris.capstone.shared.application.policy.domain.identity.defaults;

import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.DomainRefResolver;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdSpec;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.RawParser;
import com.lilamaris.capstone.shared.domain.id.DomainId;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.type.DomainType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultDomainRefResolver<T extends DomainId<?>, R> implements DomainRefResolver<T> {
    private final DomainType type;
    private final RawParser<R> rawParser;
    private final IdSpec<T, R> idSpec;

    @Override
    public DomainType supports() {
        return type;
    }

    @Override
    public T resolve(DomainRef ref) {
        if (!type.equals(ref.type())) {
            throw new IllegalArgumentException(
                    "Unsupported DomainRef: " + ref
            );
        }
        return idSpec.fromRaw(rawParser.parse(ref.id().asString()));
    }
}
