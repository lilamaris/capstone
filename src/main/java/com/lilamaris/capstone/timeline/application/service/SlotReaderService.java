package com.lilamaris.capstone.timeline.application.service;

import com.lilamaris.capstone.shared.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.DomainRefResolverDirectory;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.type.InternalAggregateDomainType;
import com.lilamaris.capstone.timeline.application.port.in.SlotEntry;
import com.lilamaris.capstone.timeline.application.port.in.SlotPathEntry;
import com.lilamaris.capstone.timeline.application.port.in.SlotPathResolver;
import com.lilamaris.capstone.timeline.application.port.in.SlotReader;
import com.lilamaris.capstone.timeline.application.port.out.SlotQuery;
import com.lilamaris.capstone.timeline.domain.Slot;
import com.lilamaris.capstone.timeline.domain.SlotClosure;
import com.lilamaris.capstone.timeline.domain.id.SlotId;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public SlotEntry getById(ExternalizableId id) {
        var slotId = refDir.resolve(id, InternalAggregateDomainType.SLOT, SlotId.class);
        return slotQuery.getSlotById(slotId)
                .map(SlotEntry::from)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Slot with id '%s' not found.", id
                )));
    }

    @Override
    public List<SlotPathEntry> getHierarchy(ExternalizableId slotId) {
        var id = refDir.resolve(slotId, InternalAggregateDomainType.SLOT, SlotId.class);

        var closures = slotQuery.getClosureOf(id);
        var ancestorSlotIds = closures.stream().map(SlotClosure::getAncestorSlotId).toList();

        var slots = slotQuery.getSlotByIds(ancestorSlotIds).stream()
                .collect(Collectors.toUnmodifiableMap(
                        Slot::id,
                        Function.identity()
                ));

        return closures.stream()
                .map(closure -> SlotPathEntry.from(
                        slots.get(closure.getAncestorSlotId()),
                        closure
                ))
                .toList();
    }

    @Override
    public Optional<SlotPathEntry> getParent(ExternalizableId slotId) {
        var id = refDir.resolve(slotId, InternalAggregateDomainType.SLOT, SlotId.class);
        var closure = slotQuery.getParentOf(id).orElse(null);
        if (closure == null) return Optional.empty();
        return slotQuery.getSlotById(closure.getAncestorSlotId())
                .map(s -> SlotPathEntry.from(s, closure));
    }
}
