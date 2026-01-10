package com.lilamaris.capstone.snapshot.application.service;

import com.lilamaris.capstone.shared.application.exception.ResourceNotFoundException;
import com.lilamaris.capstone.snapshot.application.port.out.SnapshotPort;
import com.lilamaris.capstone.snapshot.domain.Snapshot;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SnapshotResourceUtil {
    private final SnapshotPort snapshotPort;

    public Snapshot getSnapshot(SnapshotId id) {
        return snapshotPort.getById(id).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Snapshot with id '%s' not found.", id))
        );
    }
}
