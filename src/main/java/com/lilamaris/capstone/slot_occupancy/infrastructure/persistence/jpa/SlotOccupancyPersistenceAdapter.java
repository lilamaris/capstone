package com.lilamaris.capstone.slot_occupancy.infrastructure.persistence.jpa;

import com.lilamaris.capstone.slot_occupancy.application.port.out.SlotOccupancyPort;
import com.lilamaris.capstone.slot_occupancy.domain.SlotOccupancy;
import com.lilamaris.capstone.slot_occupancy.domain.id.SlotOccupancyId;
import com.lilamaris.capstone.slot_occupancy.infrastructure.persistence.jpa.repository.SlotOccupancyRepository;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.timeline.domain.id.SlotId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SlotOccupancyPersistenceAdapter implements SlotOccupancyPort {
    private final SlotOccupancyRepository repository;

    @Override
    public boolean existsBySlotIdOrSnapshotId(SlotId slotId, SnapshotId snapshotId) {
        return repository.existsBySnapshotSlotIdOrSnapshotId(slotId, snapshotId);
    }

    @Override
    public Optional<SlotOccupancy> getById(SlotOccupancyId id) {
        return repository.findById(id);
    }

    @Override
    public List<SlotOccupancy> getByIds(List<SlotOccupancyId> ids) {
        return repository.findAllById(ids);
    }

    @Override
    public Optional<SlotOccupancy> getBySlotId(SlotId slotId) {
        return repository.findBySnapshotSlotId(slotId);
    }

    @Override
    public List<SlotOccupancy> getBySlotIds(List<SlotId> slotIds) {
        return List.of();
    }

    @Override
    public Optional<SlotOccupancy> getBySnapshotId(SnapshotId snapshotId) {
        return repository.findBySnapshotId(snapshotId);
    }

    @Override
    public List<SlotOccupancy> getBySnapshotIds(List<SnapshotId> snapshotIds) {
        return List.of();
    }

    @Override
    public SlotOccupancy save(SlotOccupancy slotOccupancy) {
        return repository.save(slotOccupancy);
    }

    @Override
    public void deleteById(SlotOccupancyId id) {
        repository.deleteById(id);
    }
}
