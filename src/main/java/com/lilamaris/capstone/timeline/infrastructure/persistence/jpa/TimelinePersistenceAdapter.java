package com.lilamaris.capstone.timeline.infrastructure.persistence.jpa;

import com.lilamaris.capstone.timeline.application.port.out.TimelineStore;
import com.lilamaris.capstone.timeline.domain.Slot;
import com.lilamaris.capstone.timeline.domain.Timeline;
import com.lilamaris.capstone.timeline.domain.id.SlotId;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;
import com.lilamaris.capstone.timeline.infrastructure.persistence.jpa.repository.SlotRepository;
import com.lilamaris.capstone.timeline.infrastructure.persistence.jpa.repository.TimelineRepository;
import com.lilamaris.capstone.timeline.infrastructure.persistence.jpa.specification.SlotSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TimelinePersistenceAdapter implements TimelineStore {
    private final TimelineRepository timelineRepository;
    private final SlotRepository slotRepository;

    @Override
    public List<Timeline> getAll() {
        return timelineRepository.findAll();
    }

    @Override
    public List<Timeline> getByIds(List<TimelineId> ids) {
        return timelineRepository.findAllById(ids);
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
    public Optional<Slot> getSlotById(SlotId slotId) {
        return slotRepository.findById(slotId);
    }

    @Override
    public List<Slot> getSlotByIds(List<SlotId> slotIds) {
        return slotRepository.findAllById(slotIds);
    }

    @Override
    public Optional<Timeline> getById(TimelineId id) {
        return timelineRepository.findById(id);
    }

    @Override
    @Transactional
    public Timeline save(Timeline domain) {
        return timelineRepository.save(domain);
    }
}
