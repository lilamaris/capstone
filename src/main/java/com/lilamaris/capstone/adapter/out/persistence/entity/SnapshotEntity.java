package com.lilamaris.capstone.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "snapshot")
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SnapshotEntity extends BaseEntity<UUID> {
    @Column(name = "tx_from", nullable = false)
    private LocalDateTime txFrom;

    @Column(name = "tx_to", nullable = false)
    private LocalDateTime txTo;

    @Column(name = "valid_from", nullable = false)
    private LocalDateTime validFrom;

    @Column(name = "valid_to", nullable = false)
    private LocalDateTime validTo;

    @Column(name = "version_no", nullable = false)
    private Integer versionNo;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "timeline_id", nullable = false)
    private UUID timelineId;
}