package com.lilamaris.capstone.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
    @Column(name = "tx_from")
    private LocalDateTime txFrom;

    @Column(name = "tx_to")
    private LocalDateTime txTo;

    @Column(name = "valid_from")
    private LocalDateTime validFrom;

    @Column(name = "valid_to")
    private LocalDateTime validTo;

    @Column(name = "version_no")
    private Integer versionNo;

    @Column(name = "description")
    private String description;

    @ManyToOne
    private TimelineEntity timeline;
}