package com.lilamaris.capstone.timeline.infrastructure.persistence.jpa;

import com.lilamaris.capstone.shared.application.util.UniversityClock;
import com.lilamaris.capstone.snapshot.domain.Snapshot;
import com.lilamaris.capstone.snapshot.infrastructure.persistence.jpa.repository.SnapshotRepository;
import com.lilamaris.capstone.timeline.application.condition.SnapshotQueryCondition;
import com.lilamaris.capstone.timeline.application.port.out.TimelinePort;
import com.lilamaris.capstone.timeline.domain.Timeline;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;
import com.lilamaris.capstone.timeline.infrastructure.persistence.jpa.repository.TimelineRepository;
import com.lilamaris.capstone.timeline.infrastructure.persistence.jpa.specification.TimelineSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TimelinePersistenceAdapter implements TimelinePort {
    private final TimelineRepository timelineRepository;
    private final SnapshotRepository snapshotRepository;

    @Override
    public List<Timeline> getAll() {
        return timelineRepository.findAll();
    }

    @Override
    public List<Snapshot> getSnapshot(SnapshotQueryCondition condition) {
        Specification<Snapshot> spec = Specification.unrestricted();

        spec = spec.and(TimelineSpecification.timelineEqual(condition.timelineId()));
        if (condition.hasValidAt())
            spec = spec.and(TimelineSpecification.betweenValid(UniversityClock.at(condition.validAt())));
        if (condition.hasTxAt()) spec = spec.and(TimelineSpecification.betweenTx(UniversityClock.at(condition.txAt())));
        else spec = spec.and(TimelineSpecification.isOpenTx());

        return snapshotRepository.findAll(spec);
    }

    @Override
    public Optional<Timeline> getById(TimelineId id) {
        return timelineRepository.findById(id);
    }

    @Override
    public List<Timeline> getByIds(List<TimelineId> ids) {
        return timelineRepository.findAllById(ids);
    }

    @Override
    @Transactional
    public Timeline save(Timeline domain) {
        return timelineRepository.save(domain);
    }
}
