package com.lilamaris.capstone.timeline.infrastructure.persistence.jpa;

import com.lilamaris.capstone.timeline.application.port.out.TimelinePort;
import com.lilamaris.capstone.timeline.domain.Slot;
import com.lilamaris.capstone.timeline.domain.Timeline;
import com.lilamaris.capstone.timeline.domain.id.SlotId;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;
import com.lilamaris.capstone.timeline.infrastructure.persistence.jpa.repository.SnapshotSlotRepository;
import com.lilamaris.capstone.timeline.infrastructure.persistence.jpa.repository.TimelineRepository;
import com.lilamaris.capstone.timeline.infrastructure.persistence.jpa.specification.SnapshotSlotSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TimelinePersistenceAdapter implements TimelinePort {
    private final TimelineRepository timelineRepository;
    private final SnapshotSlotRepository snapshotSlotRepository;

    @Override
    public List<Timeline> getAll() {
        return timelineRepository.findAll();
    }

    @Override
    public List<Timeline> getAllByIds(List<TimelineId> ids) {
        return timelineRepository.findAllById(ids);
    }

    @Override
    public List<Slot> getSlotsByTxTime(TimelineId id, Instant txAt) {
        Specification<Slot> spec = Specification.unrestricted();
        spec = spec.and(SnapshotSlotSpecification.timelineEqual(id));
        spec = spec.and(SnapshotSlotSpecification.betweenTx(txAt));

        return snapshotSlotRepository.findAll(spec);
    }

    @Override
    public Optional<Slot> getSlot(SlotId slotId) {
        return snapshotSlotRepository.findById(slotId);
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
