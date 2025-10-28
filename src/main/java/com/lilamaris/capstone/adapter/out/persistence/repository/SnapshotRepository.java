package com.lilamaris.capstone.adapter.out.persistence.repository;

import com.lilamaris.capstone.adapter.out.persistence.entity.SnapshotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SnapshotRepository extends JpaRepository<SnapshotEntity, UUID> {
    boolean existsByTimeline_Id(UUID timelineId);

    @Query("""
            SELECT se
            FROM SnapshotEntity se
            WHERE se.txFrom <= :txAt
                AND se.txTo > :txAt
            ORDER BY se.validTo ASC""")
    List<SnapshotEntity> findTxAt(@Param("txAt") LocalDateTime txAt);

    @Query("""
            SELECT se
            FROM SnapshotEntity se
            WHERE se.validFrom <= :validAt
                AND se.validTo > :validAt
            ORDER BY se.versionNo DESC""")
    List<SnapshotEntity> findValidAt(@Param("validAt") LocalDateTime validAt);
}
