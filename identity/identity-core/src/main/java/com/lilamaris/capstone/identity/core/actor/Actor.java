package com.lilamaris.capstone.identity.core.actor;

import java.util.Set;

public interface Actor {
    String subject();

    Set<Capability> capabilities();
}
