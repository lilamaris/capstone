package com.lilamaris.capstone.adapter.out.persistence;

import com.lilamaris.capstone.adapter.out.persistence.mapper.TimelineEntityMapper;
import com.lilamaris.capstone.adapter.out.persistence.repository.TimelineRepository;
import com.lilamaris.capstone.application.port.out.TimelinePort;
import com.lilamaris.capstone.domain.timeline.Timeline;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TimelinePersistenceAdapter implements TimelinePort {
    private final TimelineRepository repository;

    @Override
    public Optional<Timeline> getById(Timeline.Id id) {
        return repository.findById(id.value()).map(TimelineEntityMapper::toDomain);
    }

    @Override
    public List<Timeline> getByIds(List<Timeline.Id> ids) {
        return repository.findAllById(ids.stream().map(Timeline.Id::value).toList()).stream().map(TimelineEntityMapper::toDomain).toList();
    }

    @Override
    @Transactional
    public Timeline save(Timeline domain) {
        var entity = TimelineEntityMapper.toEntity(domain);
        var saved = repository.save(entity);
        return TimelineEntityMapper.toDomain(saved);
    }
}
