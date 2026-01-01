package com.lilamaris.capstone.timeline.infrastructure.persistence.jpa.repository;

import com.lilamaris.capstone.timeline.domain.Timeline;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimelineRepository extends JpaRepository<Timeline, TimelineId> {
}
