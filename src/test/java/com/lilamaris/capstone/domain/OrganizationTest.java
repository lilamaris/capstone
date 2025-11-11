package com.lilamaris.capstone.domain;

import com.lilamaris.capstone.domain.degree.Organization;
import com.lilamaris.capstone.domain.degree.OrganizationTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class OrganizationTest {
    private Organization org;

    @BeforeEach
    void setup() {
        org = Organization.createRoot("Root");
    }

    @Test
    void childTree() {
        var childNum = 5;
        var lv1Child = Stream
                .iterate(0, i -> i + 1)
                .limit(childNum)
                .map(i -> Organization.createChildOf(org, "Lv1 Child #" + i.toString()))
                .toList();

        var lv2Child = lv1Child.stream()
                .flatMap(
                parent -> Stream
                    .iterate(0, i -> i + 1)
                    .limit(childNum)
                    .map(i -> Organization.createChildOf(parent, "Lv2 Child #" + i.toString()))
                )
                .toList();

        List<Organization> orgList = new ArrayList<>(lv1Child);
        orgList.addAll(lv2Child);
        orgList.add(org);

        var tree = OrganizationTree.from(orgList);

        assertThat(lv1Child.size()).isEqualTo(childNum);
        assertThat(lv2Child.size()).isEqualTo(childNum * childNum);
        assertThat(tree.children().size()).isEqualTo(1 + childNum);
    }
}
