package com.lilamaris.capstone.snapshot.application.service;

import com.lilamaris.capstone.scenario.occupancy.application.port.out.OccupancySnapshotEntry;
import com.lilamaris.capstone.scenario.occupancy.application.port.out.OccupancySnapshotQuery;
import com.lilamaris.capstone.shared.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.DomainRefResolverDirectory;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.snapshot.application.port.out.SnapshotPort;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SnapshotQueryService implements
        OccupancySnapshotQuery {
    private final SnapshotPort snapshotPort;
    private final DomainRefResolverDirectory refs;

    @Override
    public OccupancySnapshotEntry getEntry(DomainRef ref) {
        var id = refs.resolve(ref, SnapshotId.class);
        var snapshot = snapshotPort.getById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Snapshot with id '%s' not found.", id
                )));

        return OccupancySnapshotEntry.from(snapshot);
    }

    @Override
    public List<OccupancySnapshotEntry> getEntries(List<DomainRef> refs) {
        var ids = refs.stream().map(ref -> this.refs.resolve(ref, SnapshotId.class)).toList();
        var snapshots = snapshotPort.getByIds(ids);
        return snapshots.stream().map(OccupancySnapshotEntry::from).toList();
    }
}
