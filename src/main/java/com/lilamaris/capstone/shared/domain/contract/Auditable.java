package com.lilamaris.capstone.shared.domain.contract;

import com.lilamaris.capstone.shared.domain.metadata.AuditMetadata;

public interface Auditable {
    AuditMetadata auditMetadata();
}
