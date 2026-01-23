package com.lilamaris.capstone.timeline.application.service;

import com.lilamaris.capstone.shared.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.DomainRefResolverDirectory;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.timeline.application.port.in.SlotEntry;
import com.lilamaris.capstone.timeline.application.port.in.SlotPathEntry;
import com.lilamaris.capstone.timeline.application.port.in.SlotPathResolver;
import com.lilamaris.capstone.timeline.application.port.in.SlotReader;
import com.lilamaris.capstone.timeline.application.port.out.SlotQuery;
import com.lilamaris.capstone.timeline.domain.id.SlotId;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SlotReaderService implements
        SlotReader,
        SlotPathResolver {
    private final SlotQuery slotQuery;
    private final DomainRefResolverDirectory refDir;

    @Override
    public List<SlotEntry> getByTimelineInTxTime(TimelineId timelineId, Instant at) {
        return slotQuery.getSlotsByTxTime(timelineId, at).stream()
                .map(SlotEntry::from)
                .toList();
    }

    @Override
    public List<SlotEntry> getByTimelineInValidTime(TimelineId timelineId, Instant at) {
        return slotQuery.getSlotsByValidTime(timelineId, at).stream()
                .map(SlotEntry::from)
                .toList();
    }

    @Override
    public List<SlotEntry> resolveRefs(List<DomainRef> refs) {
        var ids = refDir.resolve(refs, SlotId.class);
        return slotQuery.getSlotByIds(ids).stream().map(SlotEntry::from).toList();
    }

    @Override
    public SlotEntry resolveRef(DomainRef ref) {
        var id = refDir.resolve(ref, SlotId.class);
        return slotQuery.getSlotById(id)
                .map(SlotEntry::from)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Slot with snapshotRef '%s' not found.", id
                )));
    }

    @Override
    public List<SlotPathEntry> getPathOf(SlotId slotId) {
        return slotQuery.getClosureOf(slotId).stream()
                .map(SlotPathEntry::from)
                .toList();
    }
}
