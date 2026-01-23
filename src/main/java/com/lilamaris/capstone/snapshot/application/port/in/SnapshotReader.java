package com.lilamaris.capstone.snapshot.application.port.in;

import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;

import java.util.List;

public interface SnapshotReader {
    List<SnapshotEntry> resolveRefs(List<DomainRef> refs);

    SnapshotEntry resolveRef(DomainRef ref);

    SnapshotEntry getById(SnapshotId id);

    List<SnapshotEntry> getByIds(List<SnapshotId> ids);
}
