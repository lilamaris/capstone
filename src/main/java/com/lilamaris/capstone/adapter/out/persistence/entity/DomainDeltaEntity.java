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
public class DomainDeltaEntity extends BaseAuditableEntity<UUID> {
    @Column(name = "snapshot_link_id")
    private UUID snapshotLinkId;

    @Column(name = "domain_type")
    private String domainType;

    @Column(name = "domain_id")
    private String domainId;

    @Column(name = "patch")
    private String patch;
}
