package com.lilamaris.capstone.adapter.out.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "snapshot_link")
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SnapshotLinkEntity extends BaseEntity<UUID> {
    @Column(name = "timeline_id", nullable = false)
    private UUID timelineId;

    @Column(name = "ancestor_snapshot_id")
    private UUID ancestorSnapshotId;

    @Column(name = "descendant_snapshot_id", nullable = false)
    private UUID descendantSnapshotId;

    @Builder.Default
    @OneToMany(mappedBy = "snapshotLinkId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DomainDeltaEntity> domainDeltaList = new ArrayList<>();
}
