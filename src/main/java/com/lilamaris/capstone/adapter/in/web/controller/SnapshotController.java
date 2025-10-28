package com.lilamaris.capstone.adapter.in.web.controller;

import com.lilamaris.capstone.adapter.in.web.request.SnapshotRequest;
import com.lilamaris.capstone.application.port.in.SnapshotUseCase;
import com.lilamaris.capstone.application.port.in.result.SnapshotResult;
import com.lilamaris.capstone.domain.Snapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/snapshot")
@RequiredArgsConstructor
public class SnapshotController {
    private final SnapshotUseCase snapshotUseCase;

    @PutMapping("/{id}")
    public ResponseEntity<SnapshotResult.Command> create(
            @PathVariable("id") UUID id,
            @RequestBody SnapshotRequest.Update body
    ) {
        return ResponseEntity.ok(snapshotUseCase.update(Snapshot.Id.from(id), body.description()));
    }
}
