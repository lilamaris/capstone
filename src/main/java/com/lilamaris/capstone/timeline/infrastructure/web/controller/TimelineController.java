package com.lilamaris.capstone.timeline.infrastructure.web.controller;

import com.lilamaris.capstone.timeline.application.port.in.*;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;
import com.lilamaris.capstone.timeline.infrastructure.web.request.TimelineRequest;
import com.lilamaris.capstone.timeline.infrastructure.web.response.SlotResponse;
import com.lilamaris.capstone.timeline.infrastructure.web.response.TimelineResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/timeline")
@RequiredArgsConstructor
public class TimelineController {
    private final TimelineReader reader;
    private final TimelineCreator creator;
    private final TimelineUpdater updater;
    private final TimelineMigration migration;
    private final TimelineMerge merge;

    private final SlotReader slotReader;

    @GetMapping
    public ResponseEntity<?> getAll(

    ) {
        var result = reader.getAll();
        return ResponseEntity.ok(
                result.stream().map(TimelineResponse::from).toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("id") UUID id
    ) {
        var timelineId = new TimelineId(id);
        var result = reader.getById(timelineId);
        return ResponseEntity.ok(
                TimelineResponse.from(result)
        );
    }

    @GetMapping("/{id}/slot")
    public ResponseEntity<?> getSlotByTxTime(
            @PathVariable("id") UUID id,
            @RequestParam(name = "tx")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant tx
    ) {
        var timelineId = new TimelineId(id);
        var result = slotReader.getByTimelineInTxTime(timelineId, tx);
        return ResponseEntity.ok(
                result.stream().map(SlotResponse::from).toList()
        );
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody TimelineRequest.Create body
    ) {
        var result = creator.create(body.title(), body.details(), body.validAt());
        return ResponseEntity.ok(
                TimelineResponse.from(result)
        );
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> migrate(
            @PathVariable("id") UUID id,
            @RequestBody TimelineRequest.Migrate body
    ) {
        var timelineId = new TimelineId(id);
        var result = migration.migrate(timelineId, body.validAt());
        return ResponseEntity.ok(
                TimelineResponse.from(result)
        );
    }

    @PostMapping("/{id}/merge")
    public ResponseEntity<?> merge(
            @PathVariable("id") UUID id,
            @RequestBody TimelineRequest.Merge body
    ) {
        var timelineId = new TimelineId(id);
        var result = merge.merge(timelineId, body.validFrom(), body.validTo());
        return ResponseEntity.ok(
                TimelineResponse.from(result)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("id") UUID id,
            @RequestBody TimelineRequest.Update body
    ) {
        var timelineId = new TimelineId(id);
        var result = updater.update(timelineId, body.title(), body.details());
        return ResponseEntity.ok(
                TimelineResponse.from(result)
        );
    }
}
