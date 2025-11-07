package com.lilamaris.capstone.adapter.out.persistence.entity;

import com.lilamaris.capstone.domain.transitionLog.Transition;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "transition_log_target")
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TransitionLogTargetEntity extends BaseEntity<UUID> {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "log_id")
    private TransitionLogEntity log;

    @Column(name = "domain_type")
    private String domainType;

    @Column(name = "domain_id")
    private String domainId;

    @Enumerated(EnumType.STRING)
    @Column(name = "transition_type")
    private Transition.TransitionType transitionType;

    @Builder.Default
    @OneToMany(mappedBy = "target", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TransitionLogTargetDiffEntity> diffList = new ArrayList<>();
}
