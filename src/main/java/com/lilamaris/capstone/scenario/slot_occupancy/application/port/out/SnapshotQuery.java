package com.lilamaris.capstone.scenario.slot_occupancy.application.port.out;

import com.lilamaris.capstone.shared.domain.id.DomainRef;

import java.util.List;

public interface SnapshotQuery {
    SnapshotEntry getEntry(DomainRef ref);

    List<SnapshotEntry> getEntries(List<DomainRef> refs);
}
