package com.lilamaris.capstone.application.util.generator;

import com.lilamaris.capstone.domain.model.common.id.DomainId;
import com.lilamaris.capstone.domain.model.common.id.IdGenerator;
import com.lilamaris.capstone.domain.model.common.id.IdSpec;
import com.lilamaris.capstone.domain.model.common.id.RawGenerator;
import org.springframework.stereotype.Component;

@Component
public class DefaultIdGenerator implements IdGenerator {
    @Override
    public <T extends DomainId<?>, R> T generate(IdSpec<T, R> spec, RawGenerator<R> rawGenerator) {
        return spec.fromRaw(rawGenerator.generate());
    }
}
