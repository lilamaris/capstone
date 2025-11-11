package com.lilamaris.capstone.adapter.out.persistence.repository;

import com.lilamaris.capstone.adapter.out.persistence.entity.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<OrganizationEntity, UUID> {
    List<OrganizationEntity> findByGroupId(UUID groupId);
    List<OrganizationEntity> findBySnapshotId(UUID snapshotId);
}
