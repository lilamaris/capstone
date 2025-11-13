package com.lilamaris.capstone.domain.degree_curriculum;

import com.lilamaris.capstone.domain.BaseDomain;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record Curriculum(
        Id id
) implements BaseDomain<Curriculum.Id, Curriculum> {
    public record Id(UUID value) implements BaseDomain.Id<UUID> {
        public static Id random() { return new Id(UUID.randomUUID()); }
        public static Id from(UUID value) { return new Id(value); }
    }
}
