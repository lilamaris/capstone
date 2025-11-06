package com.lilamaris.capstone.adapter.out.persistence.repository;

import com.lilamaris.capstone.adapter.out.persistence.entity.TransitionLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransitionLogRepository extends JpaRepository<TransitionLogEntity, UUID> {
}
