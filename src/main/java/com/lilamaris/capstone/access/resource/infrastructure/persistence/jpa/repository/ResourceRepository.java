package com.lilamaris.capstone.access.resource.infrastructure.persistence.jpa.repository;

import com.lilamaris.capstone.access.resource.domain.Resource;
import com.lilamaris.capstone.access.resource.domain.id.ResourceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ResourceRepository extends JpaRepository<Resource, ResourceId>, JpaSpecificationExecutor<Resource> {
}
