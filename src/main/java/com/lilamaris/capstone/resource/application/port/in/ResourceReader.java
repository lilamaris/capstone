package com.lilamaris.capstone.resource.application.port.in;

import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.type.DomainType;

import java.util.List;

public interface ResourceReader {
    boolean isExists(DomainType type, ExternalizableId id);

    List<ExternalizableId> getByType(DomainType type);
}
