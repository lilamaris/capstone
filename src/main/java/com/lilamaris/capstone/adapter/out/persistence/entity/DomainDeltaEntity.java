package com.lilamaris.capstone.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "domain_delta")
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DomainDeltaEntity extends BaseEntity<UUID> {
    @Column(name = "snapshot_id")
    private UUID snapshotId;

    @Column(name = "domain_name")
    private String domainName;

    @Column(name = "domain_id")
    private String domainId;

    @Column(name = "patch")
    private String patch;
}
