package com.lilamaris.capstone.bitemporal.timeline.infrastructure.persistence.jpa.repository;

import com.lilamaris.capstone.bitemporal.timeline.domain.Slot;
import com.lilamaris.capstone.bitemporal.timeline.domain.id.SlotId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SlotRepository extends JpaRepository<Slot, SlotId>, JpaSpecificationExecutor<Slot> {
}
