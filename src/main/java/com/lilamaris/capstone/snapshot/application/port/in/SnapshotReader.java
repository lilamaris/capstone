package com.lilamaris.capstone.snapshot.application.port.in;

import com.lilamaris.capstone.shared.domain.id.DomainRef;

import java.util.List;

public interface SnapshotReader {
    List<SnapshotEntry> resolveRefs(List<DomainRef> refs);

    SnapshotEntry resolveRef(DomainRef ref);
}
