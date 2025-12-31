package com.lilamaris.capstone.adapter.out.jpa.repository;

import com.lilamaris.capstone.domain.model.capstone.snapshot.Snapshot;
import com.lilamaris.capstone.domain.model.capstone.snapshot.id.SnapshotId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface SnapshotRepository extends JpaRepository<Snapshot, SnapshotId>, JpaSpecificationExecutor<Snapshot> {
}
