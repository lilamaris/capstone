package com.lilamaris.capstone.domain.degree;

import com.lilamaris.capstone.domain.AbstractUUIDDomainId;
import com.lilamaris.capstone.domain.DomainType;
import com.lilamaris.capstone.domain.degree_organization.Organization;
import lombok.Builder;

import java.util.*;

@Builder(toBuilder = true)
public record OrganizationTree(
        Id id,
        Organization root,
        Map<Organization.Id, List<Organization>> children
) {
    public enum Type implements DomainType {
        INSTANCE;

        @Override
        public String getName() {
            return "course-offer";
        }
    }

    public static class Id extends AbstractUUIDDomainId<Type> {
        public Id(UUID value) {
            super(value);
        }

        public Id() {
            super();
        }

        @Override
        public Type getDomainType() {
            return Type.INSTANCE;
        }
    }
}
