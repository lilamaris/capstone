package com.lilamaris.capstone.application;

import com.lilamaris.capstone.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.application.port.in.TimelineCommandUseCase;
import com.lilamaris.capstone.application.port.in.result.TimelineResult;
import com.lilamaris.capstone.application.port.out.TimelinePort;
import com.lilamaris.capstone.application.util.UniversityClock;
import com.lilamaris.capstone.domain.event.SnapshotDeltaEvent;
import com.lilamaris.capstone.domain.model.capstone.timeline.TimelineFactory;
import com.lilamaris.capstone.domain.model.capstone.timeline.embed.Effective;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TimelineCommandService implements TimelineCommandUseCase {
    private final TimelinePort timelinePort;

    private final TimelineFactory timelineFactory;

    @Override
    public TimelineResult.Command create(String title, String details) {
        var domain = timelineFactory.create(title, details);
        var created = timelinePort.save(domain);

        return TimelineResult.Command.from(created);
    }

    @Override
    public TimelineResult.Command update(TimelineId id, String title, String details) {
        var timeline = timelinePort.getById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Timeline with id '%s' not found.", id)
        ));

        return TimelineResult.Command.from(timeline);
    }

    @Override
    public TimelineResult.Command migrate(TimelineId id, LocalDateTime validAt, String details) {
        var timeline = timelinePort.getById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Timeline with id '%s' not found.", id)
        ));

        timeline.migrate(UniversityClock.now(), UniversityClock.at(validAt), details);
        var saved = timelinePort.save(timeline);

        return TimelineResult.Command.from(saved);
    }

    @Override
    public TimelineResult.Command merge(TimelineId id, LocalDateTime validFrom, LocalDateTime validTo, String details) {
        var timeline = timelinePort.getById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Timeline with id '%s' not found.", id)
        ));

        var validRange = Effective.create(
                UniversityClock.at(validFrom),
                UniversityClock.at(validTo)
        );
        timeline.mergeValidRange(UniversityClock.now(), validRange, details);
        var saved = timelinePort.save(timeline);

        return TimelineResult.Command.from(saved);
    }
}
