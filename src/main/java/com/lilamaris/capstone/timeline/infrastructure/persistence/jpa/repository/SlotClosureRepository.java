package com.lilamaris.capstone.timeline.infrastructure.persistence.jpa.repository;

import com.lilamaris.capstone.timeline.domain.SlotClosure;
import com.lilamaris.capstone.timeline.domain.id.SlotClosureId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlotClosureRepository extends JpaRepository<SlotClosure, SlotClosureId> {
}
