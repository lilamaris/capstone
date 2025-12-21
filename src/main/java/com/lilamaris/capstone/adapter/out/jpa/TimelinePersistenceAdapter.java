package com.lilamaris.capstone.adapter.out.jpa;

import com.lilamaris.capstone.adapter.out.jpa.repository.SnapshotRepository;
import com.lilamaris.capstone.adapter.out.jpa.repository.TimelineRepository;
import com.lilamaris.capstone.adapter.out.jpa.specification.TimelineSpecification;
import com.lilamaris.capstone.application.port.in.condition.SnapshotQueryCondition;
import com.lilamaris.capstone.application.port.out.TimelinePort;
import com.lilamaris.capstone.application.util.UniversityClock;
import com.lilamaris.capstone.domain.model.capstone.timeline.Snapshot;
import com.lilamaris.capstone.domain.model.capstone.timeline.Timeline;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
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
        if (condition.hasValidAt()) spec = spec.and(TimelineSpecification.betweenValid(UniversityClock.at(condition.validAt())));
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
