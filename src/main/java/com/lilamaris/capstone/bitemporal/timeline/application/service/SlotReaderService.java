package com.lilamaris.capstone.bitemporal.timeline.application.service;

import com.lilamaris.capstone.bitemporal.timeline.application.exception.SlotReaderInvariantException;
import com.lilamaris.capstone.bitemporal.timeline.application.port.in.*;
import com.lilamaris.capstone.bitemporal.timeline.application.port.out.SlotQuery;
import com.lilamaris.capstone.bitemporal.timeline.domain.Slot;
import com.lilamaris.capstone.bitemporal.timeline.domain.SlotClosure;
import com.lilamaris.capstone.bitemporal.timeline.domain.id.SlotId;
import com.lilamaris.capstone.bitemporal.timeline.domain.id.TimelineId;
import com.lilamaris.capstone.shared.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.DomainRefResolverDirectory;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.type.InternalAggregateDomainType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
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
    public List<SlotPathEntry> getHierarchy(SlotPathResolverOption option) {
        var id = refDir.resolve(option.targetSlotId(), InternalAggregateDomainType.SLOT, SlotId.class);

        var closures = slotQuery.getHierarchy(id, option);
        var ancestorSlotIds = closures.stream().map(SlotClosure::getAncestorSlotId).toList();

        var ancestorSlots = slotQuery.getSlotByIds(ancestorSlotIds).stream()
                .collect(Collectors.toUnmodifiableMap(
                        Slot::id,
                        Function.identity()
                ));

        if (ancestorSlotIds.size() != ancestorSlots.size()) {
            throw new SlotReaderInvariantException(String.format(
                    "Slot closure and Slot size mismatch. Closure size: '%s', Available slot size: '%s'.",
                    closures.size(),
                    ancestorSlots.size()
            ));
        }

        return closures.stream()
                .map(closure -> SlotPathEntry.from(
                        ancestorSlots.get(closure.getAncestorSlotId()),
                        closure
                ))
                .sorted(Comparator.comparing(SlotPathEntry::depth))
                .toList();
    }
}
