package com.lilamaris.capstone.adapter.out.jpa.repository;

import com.lilamaris.capstone.domain.model.capstone.timeline.Timeline;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimelineRepository extends JpaRepository<Timeline, TimelineId> {
}
