package com.lilamaris.capstone.timeline.infrastructure.web.controller;

import com.lilamaris.capstone.timeline.application.port.in.TimelineCommandUseCase;
import com.lilamaris.capstone.timeline.application.port.in.TimelineQueryUseCase;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;
import com.lilamaris.capstone.timeline.infrastructure.web.request.TimelineRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/timeline")
@RequiredArgsConstructor
public class TimelineController {
    private final TimelineCommandUseCase timelineCommandUseCase;
    private final TimelineQueryUseCase timelineQueryUseCase;

    private final Clock clock;

    @GetMapping
    public ResponseEntity<?> getAll(

    ) {
        var result = timelineQueryUseCase.getAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable("id") UUID id
    ) {
        var timelineId = new TimelineId(id);
        var result = timelineQueryUseCase.getById(timelineId);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody TimelineRequest.Create body
    ) {
        var result = timelineCommandUseCase.create(body.title(), body.details(), body.validAt());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> migrate(
            @PathVariable("id") UUID id,
            @RequestBody TimelineRequest.Migrate body
    ) {
        var timelineId = new TimelineId(id);
        var result = timelineCommandUseCase.migrate(timelineId, body.validAt());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/merge")
    public ResponseEntity<?> merge(
            @PathVariable("id") UUID id,
            @RequestBody TimelineRequest.Merge body
    ) {
        var timelineId = new TimelineId(id);
        var result = timelineCommandUseCase.merge(timelineId, body.validFrom(), body.validTo());
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable("id") UUID id,
            @RequestBody TimelineRequest.Update body
    ) {
        var timelineId = new TimelineId(id);
        var result = timelineCommandUseCase.update(timelineId, body.title(), body.details());
        return ResponseEntity.ok(result);
    }
}
