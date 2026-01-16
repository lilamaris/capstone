package com.lilamaris.capstone.snapshot.infrastructure.web.controller;

import com.lilamaris.capstone.snapshot.application.port.in.SnapshotCommandUseCase;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.snapshot.infrastructure.web.request.SnapshotRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/snapshot")
@RequiredArgsConstructor
public class SnapshotController {
    private final SnapshotCommandUseCase snapshotCommandUseCase;

    @PostMapping
    public ResponseEntity<?> createSnapshot(
            @RequestBody SnapshotRequest.Create body
    ) {
        var result = snapshotCommandUseCase.create(
                body.title(),
                body.details()
        );

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSnapshot(
            @PathVariable("id") UUID id,
            @RequestBody SnapshotRequest.Update body
    ) {
        var snapshotId = new SnapshotId(id);

        var result = snapshotCommandUseCase.update(
                snapshotId,
                body.title(),
                body.details()
        );

        return ResponseEntity.ok(result);
    }
}
