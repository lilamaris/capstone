package com.lilamaris.capstone.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "timeline")
@SuperBuilder(toBuilder = true)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TimelineEntity extends BaseEntity<UUID> {
    @Column(name = "description")
    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "timeline")
    private List<SnapshotEntity> snapshotList = List.of();
}
