package com.lilamaris.capstone.shared.domain.defaults;

import com.lilamaris.capstone.shared.domain.id.AbstractDomainId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class DefaultStringDomainId extends AbstractDomainId<String> {
    protected DefaultStringDomainId(String value) {
        init(value);
    }

    protected abstract void init(String value);
}
