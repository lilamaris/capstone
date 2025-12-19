package com.lilamaris.capstone.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public interface DomainType {
    @JsonValue
    String getName();
}
