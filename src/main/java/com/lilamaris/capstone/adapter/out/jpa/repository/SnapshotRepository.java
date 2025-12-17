package com.lilamaris.capstone.adapter.out.jpa.repository;

import com.lilamaris.capstone.adapter.out.jpa.entity.SnapshotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface SnapshotRepository extends JpaRepository<SnapshotEntity, UUID>, JpaSpecificationExecutor<SnapshotEntity> {
}
