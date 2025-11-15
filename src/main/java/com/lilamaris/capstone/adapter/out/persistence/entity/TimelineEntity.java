package com.lilamaris.capstone.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "timeline")
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TimelineEntity extends BaseEntity<UUID> {
    @Column(name = "description")
    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "timelineId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SnapshotEntity> snapshotList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "timelineId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SnapshotLinkEntity> snapshotLinkList = new ArrayList<>();
}
