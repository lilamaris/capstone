package com.lilamaris.capstone.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "course_offer")
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CourseOfferEntity extends BaseAuditableEntity<UUID> {
    @Column(name = "course_id", nullable = false)
    private UUID courseId;

    @Column(name = "semester", nullable = false)
    private Integer semester;
}
