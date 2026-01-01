package com.lilamaris.capstone.snapshot.infrastructure.persistence.jpa.repository;

import com.lilamaris.capstone.snapshot.domain.Snapshot;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface SnapshotRepository extends JpaRepository<Snapshot, SnapshotId>, JpaSpecificationExecutor<Snapshot> {
}
