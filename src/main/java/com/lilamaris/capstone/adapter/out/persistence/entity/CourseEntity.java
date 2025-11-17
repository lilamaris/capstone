package com.lilamaris.capstone.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "course")
@SuperBuilder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CourseEntity extends BaseAuditableEntity<UUID> {
    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "credit")
    private Integer credit;

    @Builder.Default
    @OneToMany(mappedBy = "courseId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseOfferEntity> courseOfferList = new ArrayList<>();
}
