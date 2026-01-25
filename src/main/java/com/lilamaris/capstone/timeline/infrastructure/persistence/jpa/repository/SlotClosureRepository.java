package com.lilamaris.capstone.timeline.infrastructure.persistence.jpa.repository;

import com.lilamaris.capstone.timeline.domain.SlotClosure;
import com.lilamaris.capstone.timeline.domain.id.SlotClosureId;
import com.lilamaris.capstone.timeline.domain.id.SlotId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SlotClosureRepository extends JpaRepository<SlotClosure, SlotClosureId>, JpaSpecificationExecutor<SlotClosure> {
}
