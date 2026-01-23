package com.lilamaris.capstone.timeline.infrastructure.persistence.jpa;

import com.lilamaris.capstone.timeline.application.port.out.SlotQuery;
import com.lilamaris.capstone.timeline.domain.Slot;
import com.lilamaris.capstone.timeline.domain.SlotClosure;
import com.lilamaris.capstone.timeline.domain.id.SlotId;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;
import com.lilamaris.capstone.timeline.infrastructure.persistence.jpa.repository.SlotClosureRepository;
import com.lilamaris.capstone.timeline.infrastructure.persistence.jpa.repository.SlotRepository;
import com.lilamaris.capstone.timeline.infrastructure.persistence.jpa.specification.SlotSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SlotPersistenceAdapter implements SlotQuery {
    private final SlotRepository slotRepository;
    private final SlotClosureRepository slotClosureRepository;

    @Override
    public Optional<Slot> getSlotById(SlotId slotId) {
        return slotRepository.findById(slotId);
    }

    @Override
    public List<Slot> getSlotByIds(List<SlotId> slotIds) {
        return slotRepository.findAllById(slotIds);
    }

    @Override
    public List<Slot> getSlotsByTxTime(TimelineId id, Instant txAt) {
        Specification<Slot> spec = Specification.unrestricted();
        spec = spec.and(SlotSpecification.timelineEqual(id));
        spec = spec.and(SlotSpecification.betweenTx(txAt));
        return slotRepository.findAll(spec);
    }

    @Override
    public List<Slot> getSlotsByValidTime(TimelineId id, Instant validAt) {
        Specification<Slot> spec = Specification.unrestricted();
        spec = spec.and(SlotSpecification.timelineEqual(id));
        spec = spec.and(SlotSpecification.betweenValid(validAt));
        return slotRepository.findAll(spec);
    }

    @Override
    public List<SlotClosure> getClosureOf(SlotId descendantSlotId) {
        return slotClosureRepository.findClosure(descendantSlotId);
    }
}
