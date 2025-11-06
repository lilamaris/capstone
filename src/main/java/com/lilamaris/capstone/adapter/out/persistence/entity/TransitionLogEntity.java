package com.lilamaris.capstone.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "transition_log")
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TransitionLogEntity extends BaseEntity<UUID> {
    @Column(name = "root_type")
    private String rootType;

    @Column(name = "root_id")
    private String rootId;

    @Column(name = "performed_by")
    private String performedBy;

    @Column(name = "performed_at")
    private LocalDateTime performedAt;

    @Column(name = "reason")
    private String reason;

    @Builder.Default
    @OneToMany(mappedBy = "log", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TransitionLogTargetEntity> targets = new ArrayList<>();
}
