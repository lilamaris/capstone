package com.lilamaris.capstone.domain;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Timeline {
    private Id id;

    private List<Snapshot.Id> snapshotIdList;

    public record Id(UUID value) {
        public static Id random() { return new Id(UUID.randomUUID()); }
        public static Id from(UUID value) { return new Id(value); }
    }
}
