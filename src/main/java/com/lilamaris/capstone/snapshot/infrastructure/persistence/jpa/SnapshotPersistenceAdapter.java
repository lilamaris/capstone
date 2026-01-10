package com.lilamaris.capstone.snapshot.infrastructure.persistence.jpa;

import com.lilamaris.capstone.snapshot.application.port.out.SnapshotPort;
import com.lilamaris.capstone.snapshot.domain.Snapshot;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.snapshot.infrastructure.persistence.jpa.repository.SnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SnapshotPersistenceAdapter implements SnapshotPort {
    private final SnapshotRepository repository;

    @Override
    public Optional<Snapshot> getById(SnapshotId id) {
        return repository.findById(id);
    }

    @Override
    public List<Snapshot> getByIds(List<SnapshotId> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public Snapshot save(Snapshot snapshot) {
        return repository.save(snapshot);
    }

    @Override
    public void delete(SnapshotId id) {
        repository.deleteById(id);
    }
}
