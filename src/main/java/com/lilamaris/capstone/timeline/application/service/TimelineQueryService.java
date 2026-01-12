package com.lilamaris.capstone.timeline.application.service;

import com.lilamaris.capstone.scenario.slot_occupancy.application.port.out.SnapshotSlotEntry;
import com.lilamaris.capstone.scenario.slot_occupancy.application.port.out.SnapshotSlotQuery;
import com.lilamaris.capstone.shared.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.DomainRefResolverDirectory;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.timeline.application.port.in.TimelineQueryUseCase;
import com.lilamaris.capstone.timeline.application.port.out.TimelinePort;
import com.lilamaris.capstone.timeline.application.result.TimelineResult;
import com.lilamaris.capstone.timeline.domain.id.SnapshotSlotId;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimelineQueryService implements
        TimelineQueryUseCase,
        SnapshotSlotQuery {
    private final TimelinePort timelinePort;
    private final TimelineResourceUtil resourceUtil;

    private final DomainRefResolverDirectory refDir;

    @Override
    public List<TimelineResult.Query> getAll() {
        return timelinePort.getAll().stream().map(TimelineResult.Query::from).toList();
    }

    @Override
    public TimelineResult.Query getById(TimelineId id) {
        var timeline = resourceUtil.getTimeline(id);
        return TimelineResult.Query.from(timeline);
    }

    @Override
    public List<SnapshotSlotEntry> getEntryByTimelineIdInTxTime(TimelineId timelineId, Instant at) {
        return timelinePort.getSlotsByTxTime(timelineId, at).stream()
                .map(SnapshotSlotEntry::from)
                .toList();
    }

    @Override
    public SnapshotSlotEntry getEntry(DomainRef ref) {
        var id = refDir.resolve(ref, SnapshotSlotId.class);
        var snapshotSlot = timelinePort.getSlot(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "SnapshotSlot with id '%s' not found.", id
                )));

        return SnapshotSlotEntry.from(snapshotSlot);
    }
}
