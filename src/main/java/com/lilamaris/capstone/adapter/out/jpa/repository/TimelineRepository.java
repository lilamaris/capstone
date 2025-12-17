package com.lilamaris.capstone.adapter.out.jpa.repository;

import com.lilamaris.capstone.adapter.out.jpa.entity.TimelineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TimelineRepository extends JpaRepository<TimelineEntity, UUID> {
}
