package com.lilamaris.capstone.timeline.application.service;

import com.lilamaris.capstone.shared.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.timeline.application.port.in.TimelineQueryUseCase;
import com.lilamaris.capstone.timeline.application.port.out.TimelineStore;
import com.lilamaris.capstone.timeline.application.result.TimelineResult;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimelineQueryService implements TimelineQueryUseCase {
    private final TimelineStore timelineStore;

    @Override
    public List<TimelineResult.Query> getAll() {
        return timelineStore.getAll().stream().map(TimelineResult.Query::from).toList();
    }

    @Override
    public TimelineResult.Query getById(TimelineId id) {
        var timeline = timelineStore.getById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Timeline with ref '%s' not found.", id)
        ));
        return TimelineResult.Query.from(timeline);
    }
}
