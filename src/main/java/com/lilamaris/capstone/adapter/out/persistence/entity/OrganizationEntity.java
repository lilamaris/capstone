package com.lilamaris.capstone.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "organization")
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationEntity extends BaseEntity<UUID> {
    @Column(name = "parent_id")
    private UUID parentId;

    @Column(name = "group_id")
    private UUID groupId;

    @Column(name = "snapshot_id")
    private UUID snapshotId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", insertable = false, updatable = false)
    private OrganizationEntity parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrganizationEntity> children = new ArrayList<>();
}
