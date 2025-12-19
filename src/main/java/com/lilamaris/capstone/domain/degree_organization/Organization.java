package com.lilamaris.capstone.domain.degree_organization;

import com.lilamaris.capstone.domain.AbstractUUIDDomainId;
import com.lilamaris.capstone.domain.DomainType;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Builder(toBuilder = true)
public record Organization(
        Id id,
        String name,
        List<AcademicProgram> academicProgramList,
        List<Organization> parent,
        List<Organization> child

) {
    public enum Type implements DomainType {
        INSTANCE;

        @Override
        public String getName() {
            return "organization";
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

    public Organization {
        id = Optional.ofNullable(id).orElseGet(Id::new);
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
