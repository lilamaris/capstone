package com.lilamaris.capstone.domain;

import com.lilamaris.capstone.domain.degree.Organization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class OrganizationTest {
    private Organization org;

    @BeforeEach
    void setup() {
        org = Organization.createRoot("Root");

    }

    @Test
    void should_belong_org() {
        org = org.createChild("Child");

        var child = org.childOrganizationList().getFirst();

        assertThat(org.childOrganizationList().size()).isEqualTo(1);
        assertThat(child.name()).isEqualTo("Child");
        assertThat(child.parentOrgId()).isEqualTo(org.id());
        assertThat(child.treeId()).isEqualTo(org.treeId());
        assertThat(child.treeLevel()).isEqualTo(org.treeLevel() + 1);
    }

    @Test
    void should_update_child_org() {
        org = org.createChild("Child");

        var child = org.childOrganizationList().getFirst();
        var updatedChild = child.toBuilder().name("Changed Child Name").build();
        org = org.updateChild(updatedChild);
        var afterUpdatedChild = org.childOrganizationList().getFirst();

        assertThat(org.childOrganizationList().size()).isEqualTo(1);
        assertThat(afterUpdatedChild.name()).isEqualTo("Changed Child Name");
        assertThat(afterUpdatedChild.parentOrgId()).isEqualTo(org.id());
    }

    @Test
    void should_delete_child_org() {
        org = org.createChild("Child");

        var child = org.childOrganizationList().getFirst();
        org = org.deleteChild(child.id());

        assertThat(org.childOrganizationList().size()).isEqualTo(0);
    }
}
