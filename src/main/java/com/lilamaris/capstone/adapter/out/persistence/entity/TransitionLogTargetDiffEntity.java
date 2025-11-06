package com.lilamaris.capstone.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "transition_log_target_diff")
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TransitionLogTargetDiffEntity extends BaseEntity<UUID> {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id")
    private TransitionLogTargetEntity target;

    @Column(name = "field_name")
    private String fieldName;

    @Column(name = "before_value")
    private String beforeValue;

    @Column(name = "after_value")
    private String afterValue;
}
