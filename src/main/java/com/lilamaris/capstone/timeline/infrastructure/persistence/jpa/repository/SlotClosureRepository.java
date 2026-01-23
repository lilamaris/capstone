package com.lilamaris.capstone.timeline.infrastructure.persistence.jpa.repository;

import com.lilamaris.capstone.timeline.domain.SlotClosure;
import com.lilamaris.capstone.timeline.domain.id.SlotClosureId;
import com.lilamaris.capstone.timeline.domain.id.SlotId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SlotClosureRepository extends JpaRepository<SlotClosure, SlotClosureId> {
    @Query("""
            SELECT sc
            FROM SlotClosure sc
            WHERE sc.descendantSlotId = :descendantSlotId
            ORDER BY depth""")
    List<SlotClosure> findClosure(SlotId descendantSlotId);
}
