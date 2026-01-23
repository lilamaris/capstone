package com.lilamaris.capstone.delta.infrastructure.persistence.jpa.repository;

import com.lilamaris.capstone.delta.domain.Delta;
import com.lilamaris.capstone.delta.domain.id.DeltaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DeltaRepository extends JpaRepository<Delta, DeltaId>, JpaSpecificationExecutor<Delta> {
}
