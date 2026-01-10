package com.lilamaris.capstone.access_control.application.port.out;

import com.lilamaris.capstone.access_control.domain.AccessControl;
import com.lilamaris.capstone.access_control.domain.id.AccessControlId;

import java.util.Optional;

public interface AccessControlPort {
    Optional<AccessControl> getById(AccessControlId id);

    AccessControl save(AccessControl domain);

    void delete(AccessControlId id);
}
