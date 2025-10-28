package com.lilamaris.capstone.adapter.out.persistence.repository;

import com.lilamaris.capstone.adapter.out.persistence.entity.TimelineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TimelineRepository extends JpaRepository<TimelineEntity, UUID> {
}
