package com.lilamaris.capstone.application;

import com.lilamaris.capstone.application.port.in.SnapshotUseCase;
import com.lilamaris.capstone.application.port.in.result.SnapshotResult;
import com.lilamaris.capstone.application.port.out.SnapshotPort;
import com.lilamaris.capstone.domain.Snapshot;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SnapshotService implements SnapshotUseCase {
    private final SnapshotPort snapshotPort;

    @Override
    public SnapshotResult.Command update(Snapshot.Id id, String description) {
        var target = snapshotPort.getById(id).orElseThrow(EntityNotFoundException::new);

        var updated = target.toBuilder().description(description).build();
        var saved = snapshotPort.save(updated);
        return SnapshotResult.Command.from(saved);
    }
}
