package com.lilamaris.capstone.adapter.out.persistence.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.ZonedDateTime;

@Embeddable
@Builder(toBuilder = true)
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EffectiveEmbeddableEntity {
    @Column(name = "from", nullable = false)
    private ZonedDateTime from;

    @Column(name = "to", nullable = false)
    private ZonedDateTime to;

    @Column(name = "zone_id")
    private String zoneId;
}
