package com.lilamaris.capstone.domain.degree;

import com.lilamaris.capstone.domain.BaseDomain;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Builder(toBuilder = true)
public record Organization(
        Id id,
        TreeId treeId,
        Integer treeLevel,
        Id parentOrgId,
        String name,
        List<Organization> childOrganizationList,
        List<AcademicProgram.Id> academicProgramIdList
) implements BaseDomain<Organization.Id, Organization> {
    public record Id(UUID value) implements BaseDomain.Id<UUID> {
        public static Id random() { return new Id(UUID.randomUUID()); }
        public static Id from(UUID value) { return new Id(value); }
    }

    public record TreeId(UUID value) {
        public static TreeId random() { return new TreeId(UUID.randomUUID()); }
        public static TreeId from(UUID value) { return new TreeId(value); }
    }

    public Organization {
        id = Optional.ofNullable(id).orElseGet(Id::random);
        childOrganizationList = Optional.ofNullable(childOrganizationList).orElseGet(ArrayList::new);
        academicProgramIdList = Optional.ofNullable(academicProgramIdList).orElseGet(ArrayList::new);
        treeId = Optional.ofNullable(treeId).orElseThrow(() -> new IllegalArgumentException("tree id must be provided within the tree context"));
        treeLevel = Optional.ofNullable(treeLevel).orElseThrow(() -> new IllegalArgumentException("tree level must be provided within the tree context"));
        name = Optional.ofNullable(name).orElseThrow(() -> new IllegalArgumentException("name must be set"));
    }

    public static Organization createRoot(String name) {
        var treeId = TreeId.random();
        return Organization.builder().treeId(treeId).treeLevel(0).name(name).build();
    }

    public Organization createChild(String name) {
        var newOrg = Organization.builder()
                .treeId(treeId)
                .treeLevel(treeLevel + 1)
                .parentOrgId(id)
                .name(name)
                .build();

        var updatedOrg = new ArrayList<>(childOrganizationList);
        updatedOrg.add(newOrg);

        return toBuilder().childOrganizationList(updatedOrg).build();
    }

    public boolean isRoot() {
        return parentOrgId == null;
    }
}
