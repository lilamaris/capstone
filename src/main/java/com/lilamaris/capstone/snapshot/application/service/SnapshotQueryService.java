package com.lilamaris.capstone.snapshot.application.service;

import com.lilamaris.capstone.scenario.slot_occupancy.application.port.out.SnapshotEntry;
import com.lilamaris.capstone.scenario.slot_occupancy.application.port.out.SnapshotQuery;
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
        SnapshotQuery {
    private final SnapshotPort snapshotPort;
    private final DomainRefResolverDirectory refDir;

    @Override
    public SnapshotEntry getEntry(DomainRef ref) {
        var id = refDir.resolve(ref, SnapshotId.class);
        var snapshot = snapshotPort.getById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Snapshot with id '%s' not found.", id
                )));

        return SnapshotEntry.from(snapshot);
    }

    @Override
    public List<SnapshotEntry> getEntries(List<DomainRef> refs) {
        var ids = refs.stream().map(ref -> refDir.resolve(ref, SnapshotId.class)).toList();
        var snapshots = snapshotPort.getByIds(ids);
        return snapshots.stream().map(SnapshotEntry::from).toList();
    }
}
