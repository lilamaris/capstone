package com.lilamaris.capstone.domain;

import java.util.UUID;

public interface DomainId {
    UUID value();
    String getDomainName();
}
