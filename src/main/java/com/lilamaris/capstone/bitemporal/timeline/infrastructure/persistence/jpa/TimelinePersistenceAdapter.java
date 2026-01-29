package com.lilamaris.capstone.bitemporal.timeline.infrastructure.persistence.jpa;

import com.lilamaris.capstone.bitemporal.timeline.application.port.out.TimelineStore;
import com.lilamaris.capstone.bitemporal.timeline.domain.Timeline;
import com.lilamaris.capstone.bitemporal.timeline.domain.id.TimelineId;
import com.lilamaris.capstone.bitemporal.timeline.infrastructure.persistence.jpa.repository.TimelineRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TimelinePersistenceAdapter implements TimelineStore {
    private final TimelineRepository timelineRepository;

    @Override
    public List<Timeline> getAll() {
        return timelineRepository.findAll();
    }

    @Override
    public List<Timeline> getByIds(List<TimelineId> ids) {
        return timelineRepository.findAllById(ids);
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
