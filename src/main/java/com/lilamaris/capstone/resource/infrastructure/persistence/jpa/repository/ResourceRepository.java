package com.lilamaris.capstone.resource.infrastructure.persistence.jpa.repository;

import com.lilamaris.capstone.resource.domain.Resource;
import com.lilamaris.capstone.resource.domain.id.ResourceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ResourceRepository extends JpaRepository<Resource, ResourceId>, JpaSpecificationExecutor<Resource> {
}
