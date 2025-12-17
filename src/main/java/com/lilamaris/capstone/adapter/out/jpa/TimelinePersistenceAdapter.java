package com.lilamaris.capstone.adapter.out.jpa;

import com.lilamaris.capstone.adapter.out.jpa.entity.SnapshotEntity;
import com.lilamaris.capstone.adapter.out.jpa.mapper.SnapshotEntityMapper;
import com.lilamaris.capstone.adapter.out.jpa.mapper.TimelineEntityMapper;
import com.lilamaris.capstone.adapter.out.jpa.repository.SnapshotRepository;
import com.lilamaris.capstone.adapter.out.jpa.repository.TimelineRepository;
import com.lilamaris.capstone.adapter.out.jpa.specification.TimelineSpecification;
import com.lilamaris.capstone.application.port.in.condition.SnapshotQueryCondition;
import com.lilamaris.capstone.application.port.out.TimelinePort;
import com.lilamaris.capstone.application.util.UniversityClock;
import com.lilamaris.capstone.domain.timeline.Snapshot;
import com.lilamaris.capstone.domain.timeline.Timeline;
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
        return timelineRepository.findAll().stream().map(TimelineEntityMapper::toDomain).toList();
    }

    @Override
    public List<Snapshot> getSnapshot(SnapshotQueryCondition condition) {
        Specification<SnapshotEntity> spec = Specification.unrestricted();

        spec = spec.and(TimelineSpecification.timelineEqual(condition.timelineId()));
        if (condition.hasValidAt()) spec = spec.and(TimelineSpecification.betweenValid(UniversityClock.at(condition.validAt())));
        if (condition.hasTxAt()) spec = spec.and(TimelineSpecification.betweenTx(UniversityClock.at(condition.txAt())));
        else spec = spec.and(TimelineSpecification.isOpenTx());

        var entities = snapshotRepository.findAll(spec);

        return entities.stream().map(SnapshotEntityMapper::toDomain).toList();
    }

    @Override
    public Optional<Timeline> getById(Timeline.Id id) {
        return timelineRepository.findById(id.value()).map(TimelineEntityMapper::toDomain);
    }

    @Override
    public List<Timeline> getByIds(List<Timeline.Id> ids) {
        return timelineRepository.findAllById(ids.stream().map(Timeline.Id::value).toList()).stream().map(TimelineEntityMapper::toDomain).toList();
    }

    @Override
    @Transactional
    public Timeline save(Timeline domain) {
        var entity = TimelineEntityMapper.toEntity(domain);
        var saved = timelineRepository.save(entity);
        return TimelineEntityMapper.toDomain(saved);
    }
}
