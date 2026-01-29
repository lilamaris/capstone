package com.lilamaris.capstone.bitemporal.timeline.infrastructure.persistence.jpa.repository;

import com.lilamaris.capstone.bitemporal.timeline.domain.Timeline;
import com.lilamaris.capstone.bitemporal.timeline.domain.id.TimelineId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimelineRepository extends JpaRepository<Timeline, TimelineId> {
}
