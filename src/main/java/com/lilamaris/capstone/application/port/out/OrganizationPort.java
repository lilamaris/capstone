package com.lilamaris.capstone.application.port.out;

import com.lilamaris.capstone.domain.degree.Organization;

import java.util.List;
import java.util.Optional;

public interface OrganizationPort {
    Optional<Organization> getById(Organization.Id id);
    List<Organization> getByGroupId(Organization.GroupId groupId);
    Organization save(Organization domain);
}
