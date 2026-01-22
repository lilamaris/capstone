package com.lilamaris.capstone.snapshot.application.service;

import com.lilamaris.capstone.shared.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.DomainRefResolverDirectory;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.snapshot.application.port.in.SnapshotEntry;
import com.lilamaris.capstone.snapshot.application.port.in.SnapshotReader;
import com.lilamaris.capstone.snapshot.application.port.out.SnapshotStore;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SnapshotReaderService implements SnapshotReader {
    private final SnapshotStore snapshotStore;
    private final DomainRefResolverDirectory refDir;

    @Override
    public List<SnapshotEntry> resolveRefs(List<DomainRef> refs) {
        var ids = refDir.resolve(refs, SnapshotId.class);
        return snapshotStore.getByIds(ids).stream().map(SnapshotEntry::from).toList();
    }

    @Override
    public SnapshotEntry resolveRef(DomainRef ref) {
        var id = refDir.resolve(ref, SnapshotId.class);
        return snapshotStore.getById(id).map(SnapshotEntry::from)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(
                        "Snapshot with ref '%s' not found", id
                )));
    }
}
