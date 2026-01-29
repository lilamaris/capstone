package com.lilamaris.capstone.bitemporal.timeline.infrastructure.persistence.jpa.repository;

import com.lilamaris.capstone.bitemporal.timeline.domain.SlotClosure;
import com.lilamaris.capstone.bitemporal.timeline.domain.id.SlotClosureId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SlotClosureRepository extends JpaRepository<SlotClosure, SlotClosureId>, JpaSpecificationExecutor<SlotClosure> {
}
