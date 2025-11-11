package com.lilamaris.capstone.domain.degree;

import com.lilamaris.capstone.domain.BaseDomain;
import com.lilamaris.capstone.domain.timeline.Snapshot;
import lombok.Builder;

import java.util.*;

@Builder(toBuilder = true)
public record Organization(
        Id id,
        Id parentId,
        GroupId groupId,
        Snapshot.Id snapshotId,
        String name,
        List<AcademicProgram> academicProgramList
) implements BaseDomain<Organization.Id, Organization> {
    public record Id(UUID value) implements BaseDomain.Id<UUID> {
        public static Id random() { return new Id(UUID.randomUUID()); }
        public static Id from(UUID value) { return new Id(value); }
    }

    public record GroupId(UUID value) {
        public static GroupId random() { return new GroupId(UUID.randomUUID()); }
        public static GroupId from(UUID value) { return new GroupId(value); }
    }

    public Organization {
        id = Optional.ofNullable(id).orElseGet(Id::random);
        groupId = Optional.ofNullable(groupId).orElseGet(GroupId::random);
        academicProgramList = Optional.ofNullable(academicProgramList).orElseGet(ArrayList::new);
        name = Optional.ofNullable(name).orElseThrow(() -> new IllegalArgumentException("name must be set"));
    }

    public static Organization createRoot(String name) {
        return Organization.builder().name(name).build();
    }

    public static Organization createChildOf(Organization parent, String name) {
        return Organization.builder()
                .parentId(parent.id)
                .groupId(parent.groupId)
                .name(name)
                .build();
    }

    public boolean isRoot() {
        return parentId == null;
    }
}
