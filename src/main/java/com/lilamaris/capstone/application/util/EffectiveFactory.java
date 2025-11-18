package com.lilamaris.capstone.application.util;

import com.lilamaris.capstone.application.configuration.ApplicationContext;
import com.lilamaris.capstone.domain.embed.Effective;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class EffectiveFactory {
    public Effective from(LocalDateTime from, LocalDateTime to) {
        var zoneId = ApplicationContext.getSystemZone();
        return new Effective(
                from.atZone(zoneId),
                to.atZone(zoneId)
        );
    }

    public Effective openAt(LocalDateTime from) {
        var zoneId = ApplicationContext.getSystemZone();
        return new Effective(
                from.atZone(zoneId),
                Effective.MAX.withZoneSameLocal(zoneId)
        );
    }
}
