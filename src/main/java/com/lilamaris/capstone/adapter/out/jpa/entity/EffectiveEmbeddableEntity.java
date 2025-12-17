package com.lilamaris.capstone.adapter.out.jpa.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.Instant;

@Embeddable
@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EffectiveEmbeddableEntity {
    @Column(name = "from", nullable = false)
    private Instant from;

    @Column(name = "to", nullable = false)
    private Instant to;
}
