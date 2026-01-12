package com.lilamaris.capstone.scenario.slot_occupancy.infrastructure.web.controller;

import com.lilamaris.capstone.scenario.slot_occupancy.application.port.in.SlotOccupancyCommandUseCase;
import com.lilamaris.capstone.scenario.slot_occupancy.application.port.in.SlotOccupancyQueryUseCase;
import com.lilamaris.capstone.scenario.slot_occupancy.infrastructure.web.request.SlotOccupyRequest;
import com.lilamaris.capstone.snapshot.domain.id.SnapshotId;
import com.lilamaris.capstone.timeline.domain.id.SnapshotSlotId;
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
public class SnapshotSlotOccupancyController {
    private final SlotOccupancyQueryUseCase slotOccupancyQueryUseCase;
    private final SlotOccupancyCommandUseCase slotOccupancyCommandUseCase;

    @GetMapping("/{id}/view/slot-occupancy")
    public ResponseEntity<?> getSlotOccupancyById(
            @PathVariable("id") UUID timelineId,
            @RequestParam(value = "tx", required = false, defaultValue = "#{T(java.time.LocalDateTime).now()}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            ZonedDateTime txParam
    ) {
        var id = new TimelineId(timelineId);
        var tx = txParam.toInstant();
        var result = slotOccupancyQueryUseCase.getOccupancyFromSlotByTxTime(id, tx);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/slot/{slotId}/action/occupy")
    public ResponseEntity<?> attachSnapshotToSlot(
            @PathVariable("slotId") UUID snapshotSlotId,
            @RequestBody SlotOccupyRequest body
    ) {
        var slotId = new SnapshotSlotId(snapshotSlotId);
        var snapshotId = new SnapshotId(body.snapshotId());
        var result = slotOccupancyCommandUseCase.occupySlot(slotId, snapshotId);
        return ResponseEntity.ok(result);
    }
}
