package com.lilamaris.capstone.identity.core.role;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum CanonicalRole {
    GUEST(0),
    USER(10),
    MAINTAINER(20),
    ADMIN(30);

    private final int rank;

    public static List<CanonicalRole> rankOrder() {
        return Arrays.stream(values())
                .sorted(Comparator.comparingInt(CanonicalRole::getRank))
                .toList();
    }
}
