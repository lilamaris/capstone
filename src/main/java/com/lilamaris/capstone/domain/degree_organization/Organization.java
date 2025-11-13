package com.lilamaris.capstone.domain.degree_organization;

import com.lilamaris.capstone.domain.BaseDomain;
import lombok.Builder;

import java.util.*;

@Builder(toBuilder = true)
public record Organization(
        Id id,
        String name,
        List<AcademicProgram> academicProgramList,
        List<Organization> parent,
        List<Organization> child

) implements BaseDomain<Organization.Id, Organization> {
    public record Id(UUID value) implements BaseDomain.Id<UUID> {
        public static Id random() { return new Id(UUID.randomUUID()); }
        public static Id from(UUID value) { return new Id(value); }
    }

    public Organization {
        id = Optional.ofNullable(id).orElseGet(Id::random);
        academicProgramList = Optional.ofNullable(academicProgramList).orElseGet(ArrayList::new);
        name = Optional.ofNullable(name).orElseThrow(() -> new IllegalArgumentException("name must be set"));
    }

    public static Organization createRoot(String name) {
        return Organization.builder().name(name).build();
    }

    public static Organization createChildOf(Organization parent, String name) {
        return Organization.builder()
                .name(name)
                .build();
    }
}
