package com.lilamaris.capstone.snapshot.infrastructure.web.controller;

import com.lilamaris.capstone.snapshot.application.port.in.SnapshotCreator;
import com.lilamaris.capstone.snapshot.application.port.in.SnapshotUpdater;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.snapshot.infrastructure.web.request.SnapshotRequest;
import com.lilamaris.capstone.snapshot.infrastructure.web.response.SnapshotResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/snapshot")
@RequiredArgsConstructor
public class SnapshotController {
    private final SnapshotCreator creator;
    private final SnapshotUpdater updater;

    @PostMapping
    public ResponseEntity<?> createSnapshot(
            @RequestBody SnapshotRequest.Create body
    ) {
        var result = creator.create(
                body.title(),
                body.details()
        );

        return ResponseEntity.ok(
                SnapshotResponse.from(result)
        );
    }

    @PutMapping("/{snapshotRef}")
    public ResponseEntity<?> updateSnapshot(
            @PathVariable("id") UUID id,
            @RequestBody SnapshotRequest.Update body
    ) {
        var snapshotId = new SnapshotId(id);

        var result = updater.update(
                snapshotId,
                body.title(),
                body.details()
        );

        return ResponseEntity.ok(
                SnapshotResponse.from(result)
        );
    }
}
