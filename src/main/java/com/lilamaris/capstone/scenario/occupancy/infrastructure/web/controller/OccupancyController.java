package com.lilamaris.capstone.scenario.occupancy.infrastructure.web.controller;

import com.lilamaris.capstone.scenario.occupancy.application.port.in.OccupancyCommandUseCase;
import com.lilamaris.capstone.scenario.occupancy.application.port.in.OccupancyQueryUseCase;
import com.lilamaris.capstone.scenario.occupancy.infrastructure.web.request.OccupancyRequest;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.timeline.domain.id.SlotId;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/timeline")
@RequiredArgsConstructor
public class OccupancyController {
    private final OccupancyQueryUseCase occupancyQueryUseCase;
    private final OccupancyCommandUseCase occupancyCommandUseCase;

    @GetMapping("/{id}/view/slot-occupancy")
    public ResponseEntity<?> getSlotOccupancyById(
            @PathVariable("id") UUID timelineId,
            @RequestParam(value = "tx", required = false, defaultValue = "#{T(java.time.ZonedDateTime).now()}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            ZonedDateTime txParam
    ) {
        var id = new TimelineId(timelineId);
        var tx = txParam.toInstant();
        var result = occupancyQueryUseCase.getOccupancyFromSlotByTxTime(id, tx);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/slot/{slotId}/action/occupy")
    public ResponseEntity<?> attachSnapshotToSlot(
            @PathVariable("slotId") UUID snapshotSlotId,
            @RequestBody OccupancyRequest body
    ) {
        var slotId = new SlotId(snapshotSlotId);
        var snapshotId = new SnapshotId(body.snapshotId());
        var result = occupancyCommandUseCase.occupySlot(slotId, snapshotId);
        return ResponseEntity.ok(result);
    }
}
