package com.lilamaris.capstone.domain;

import lombok.*;

import java.time.Period;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Snapshot {
    private Id id;

    private Timeline.Id timelineId;

    public record Id(UUID value) {
        public static Id random() { return new Id(UUID.randomUUID()); }
        public static Id from(UUID value) { return new Id(value); }
    }
}
