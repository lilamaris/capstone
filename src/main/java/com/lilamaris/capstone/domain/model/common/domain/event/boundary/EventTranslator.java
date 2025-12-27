package com.lilamaris.capstone.domain.model.common.domain.event.boundary;

import com.lilamaris.capstone.domain.model.common.domain.event.DomainEvent;

import java.util.List;

public interface EventTranslator<E extends DomainEvent> {
    List<DomainEvent> translate(E event);

    Class<E> supports();
}
