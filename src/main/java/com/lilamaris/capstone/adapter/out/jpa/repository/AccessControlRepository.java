package com.lilamaris.capstone.adapter.out.jpa.repository;

import com.lilamaris.capstone.adapter.out.jpa.entity.AccessControlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccessControlRepository extends JpaRepository<AccessControlEntity, UUID> {
    boolean existsByUserIdAndResourceIdAndResourceTypeAndScopedRole(UUID userId, String resourceId, String resourceType, String scopedRole);
}
