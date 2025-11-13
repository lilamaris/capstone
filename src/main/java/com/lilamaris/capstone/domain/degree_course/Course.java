package com.lilamaris.capstone.domain.degree_course;

import com.lilamaris.capstone.domain.BaseDomain;
import lombok.Builder;

import java.util.Optional;
import java.util.UUID;

@Builder(toBuilder = true)
public record Course(
        Id id,
        String code,
        String name,
        Integer credit
) implements BaseDomain<Course.Id, Course> {
    public record Id(UUID value) implements BaseDomain.Id<UUID> {
        public static Id random() { return new Id(UUID.randomUUID()); }
        public static Id from(UUID value) { return new Id(value); }
    }

    public Course {
        code = Optional.ofNullable(code).orElseThrow(() -> new IllegalArgumentException("code must be set"));
        name = Optional.ofNullable(name).orElseThrow(() -> new IllegalArgumentException("name must be set"));
        credit = Optional.ofNullable(credit).orElseThrow(() -> new IllegalArgumentException("credit must be set"));
    }

    public static Course from(Id id, String code, String name, Integer credit) {
        return Course.builder().id(id).code(code).name(name).credit(credit).build();
    }
}
