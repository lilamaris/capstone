package com.lilamaris.capstone.scenario.occupancy.application.port.out;

import com.lilamaris.capstone.shared.domain.id.DomainRef;

import java.util.List;

public interface OccupancySnapshotQuery {
    OccupancySnapshotEntry getEntry(DomainRef ref);

    List<OccupancySnapshotEntry> getEntries(List<DomainRef> refs);
}
