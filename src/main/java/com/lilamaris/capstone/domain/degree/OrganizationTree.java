package com.lilamaris.capstone.domain.degree;

import com.lilamaris.capstone.domain.degree_organization.Organization;
import lombok.Builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Builder(toBuilder = true)
public record OrganizationTree(
    Organization root,
    Map<Organization.Id, List<Organization>> children
) {
    public List<Organization> childrenOf(Organization.Id id) {
        return children.getOrDefault(id, List.of());
    }

    public Organization find(Organization.Id id) {
        if (root.id().equals(id)) return root;
        return children.values().stream().flatMap(List::stream)
                .filter(o -> o.id().equals(id))
                .findFirst().orElseThrow();
    }

    public OrganizationTree addChild(Organization parent, Organization child) {
        var newChildren = new HashMap<>(children);
        newChildren.computeIfAbsent(parent.id(), k -> new ArrayList<>()).add(child);
        return new OrganizationTree(root, newChildren);
    }
}
