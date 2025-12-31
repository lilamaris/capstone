package com.lilamaris.capstone.domain.model.common.domain.contract;

import com.lilamaris.capstone.domain.model.common.domain.metadata.AuditMetadata;

public interface Auditable {
    AuditMetadata auditMetadata();
}
