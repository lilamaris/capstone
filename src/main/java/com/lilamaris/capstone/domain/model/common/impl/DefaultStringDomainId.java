package com.lilamaris.capstone.domain.model.common.impl;

import com.lilamaris.capstone.domain.model.common.AbstractDomainId;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class DefaultStringDomainId extends AbstractDomainId<String> {
    protected DefaultStringDomainId(String value) {
        init(value);
    }

    protected abstract void init(String value);
}
