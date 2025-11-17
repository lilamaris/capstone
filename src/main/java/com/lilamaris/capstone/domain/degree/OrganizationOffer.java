package com.lilamaris.capstone.domain.degree;

import com.lilamaris.capstone.domain.BaseDomain;
import com.lilamaris.capstone.domain.degree_organization.Organization;
import com.lilamaris.capstone.domain.timeline.Snapshot;
import lombok.Builder;

import java.util.UUID;

@Builder(toBuilder = true)
public record OrganizationOffer(
        Id id,
        Organization.Id rootOrganizationId,
        Snapshot.Id targetSnapshotId
) implements BaseDomain<OrganizationOffer.Id, OrganizationOffer> {
    public record Id(UUID value) implements BaseDomain.Id<UUID> {
        public static Id random() { return new Id(UUID.randomUUID()); }
        public static Id from(UUID value) { return new Id(value); }
    }
}
