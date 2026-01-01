package com.lilamaris.capstone.shared.application.event;

import com.lilamaris.capstone.shared.domain.event.DomainEvent;
import com.lilamaris.capstone.shared.domain.event.boundary.EventTranslator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class EventTranslatorRegistry {
    private final Map<Class<? extends DomainEvent>, EventTranslator<?>> translators;

    public EventTranslatorRegistry(List<EventTranslator<?>> translators) {
        this.translators = translators.stream()
                .collect(Collectors.toMap(
                        EventTranslator::supports,
                        Function.identity()
                ));
    }

    public List<DomainEvent> translate(DomainEvent event) {
        @SuppressWarnings("unchecked")
        var translator = (EventTranslator<DomainEvent>) translators.get(event.getClass());
        if (translator == null) return List.of();
        return translator.translate(event);
    }
}
