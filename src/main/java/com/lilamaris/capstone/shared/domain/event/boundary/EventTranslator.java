package com.lilamaris.capstone.shared.domain.event.boundary;

import com.lilamaris.capstone.shared.domain.event.DomainEvent;

import java.util.List;

public interface EventTranslator<E extends DomainEvent> {
    List<DomainEvent> translate(E event);

    Class<E> supports();
}
