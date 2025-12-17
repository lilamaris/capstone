package com.lilamaris.capstone.adapter.out.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "snapshot")
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SnapshotEntity extends BaseEntity<UUID> {
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "from", column = @Column(name = "tx_from")),
            @AttributeOverride(name = "to", column = @Column(name = "tx_to")),
    })
    private EffectiveEmbeddableEntity tx;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "from", column = @Column(name = "valid_from")),
            @AttributeOverride(name = "to", column = @Column(name = "valid_to")),
    })
    private EffectiveEmbeddableEntity valid;

    @Column(name = "version_no", nullable = false)
    private Integer versionNo;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "timeline_id", nullable = false)
    private UUID timelineId;
}