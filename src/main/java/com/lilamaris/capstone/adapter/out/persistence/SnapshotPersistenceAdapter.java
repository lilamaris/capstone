package com.lilamaris.capstone.adapter.out.persistence;

import com.lilamaris.capstone.adapter.out.persistence.mapper.SnapshotEntityMapper;
import com.lilamaris.capstone.adapter.out.persistence.repository.SnapshotRepository;
import com.lilamaris.capstone.application.port.out.SnapshotPort;
import com.lilamaris.capstone.domain.Snapshot;
import com.lilamaris.capstone.domain.Timeline;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SnapshotPersistenceAdapter implements SnapshotPort {
    private final SnapshotRepository repository;
    private final EntityManager em;

    @Override
    public boolean isExistsInTimeline(Timeline.Id id) {
        return repository.existsByTimeline_Id(id.value());
    }

    @Override
    public Optional<Snapshot> getById(Snapshot.Id id) {
        return repository.findById(id.value()).map(SnapshotEntityMapper::toDomain);
    }

    @Override
    public List<Snapshot> getByValidAt(LocalDateTime validAt) {
        return repository.findValidAt(validAt).stream().map(SnapshotEntityMapper::toDomain).toList();
    }

    @Override
    public List<Snapshot> getByTxAt(LocalDateTime txAt) {
        return repository.findTxAt(txAt).stream().map(SnapshotEntityMapper::toDomain).toList();
    }

    @Override
    public List<Snapshot> getByIds(List<Snapshot.Id> ids) {
         return repository.findAllById(ids.stream().map(Snapshot.Id::value).toList()).stream().map(SnapshotEntityMapper::toDomain).toList();
    }

    @Override
    public Snapshot save(Snapshot domain) {
        var entity = SnapshotEntityMapper.toEntity(domain, em);
        var saved = repository.save(entity);
        return SnapshotEntityMapper.toDomain(saved);
    }

    @Override
    public List<Snapshot> save(List<Snapshot> domains) {
        var entities = domains.stream().map(domain -> SnapshotEntityMapper.toEntity(domain, em)).toList();
        var saved = repository.saveAll(entities);
        return saved.stream().map(SnapshotEntityMapper::toDomain).toList();
    }

    @Override
    public void delete(Snapshot.Id id) {
        repository.deleteById(id.value());
    }
}
