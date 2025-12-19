package com.lilamaris.capstone.adapter.out.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "access_control")
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccessControlEntity extends BaseEntity<UUID> {
    @Column(nullable = false)
    private UUID userId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "resource_id")),
            @AttributeOverride(name = "type", column = @Column(name = "resource_type")),
    })
    private DomainRefEmbeddableEntity resource;

    @Column(name = "scoped_role")
    private String scopedRole;
}
