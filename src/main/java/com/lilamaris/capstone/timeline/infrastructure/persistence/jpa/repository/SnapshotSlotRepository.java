package com.lilamaris.capstone.timeline.infrastructure.persistence.jpa.repository;

import com.lilamaris.capstone.timeline.domain.SnapshotSlot;
import com.lilamaris.capstone.timeline.domain.id.SnapshotSlotId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SnapshotSlotRepository extends JpaRepository<SnapshotSlot, SnapshotSlotId>, JpaSpecificationExecutor<SnapshotSlot> {
}
