package com.lilamaris.capstone.application.event;

import com.lilamaris.capstone.domain.model.common.domain.event.DomainEvent;
import com.lilamaris.capstone.domain.model.common.domain.event.aggregate.CollectedDomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class EventPipeline {
    private final EventTranslatorRegistry registry;
    private final ApplicationEventPublisher publisher;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(CollectedDomainEvent event) {
        for (DomainEvent e : event.events()) {
            var canonicalEvents = registry.translate(e);
            canonicalEvents.forEach(publisher::publishEvent);
        }
    }
}
