package com.lilamaris.capstone.adapter.in.web.controller;

import com.lilamaris.capstone.adapter.in.web.request.TimelineRequest;
import com.lilamaris.capstone.application.port.in.TimelineCommandUseCase;
import com.lilamaris.capstone.application.port.in.result.TimelineResult;
import com.lilamaris.capstone.domain.Snapshot;
import com.lilamaris.capstone.domain.Timeline;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/timeline")
@RequiredArgsConstructor
public class TimelineController {
    private final TimelineCommandUseCase timelineCommandUseCase;

    @PostMapping
    public ResponseEntity<TimelineResult.Command> create(
            @RequestBody TimelineRequest.Create body
    ) {
       var result = timelineCommandUseCase.create(body.description());
       return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> migrate(
            @PathVariable("id") UUID id,
            @RequestBody TimelineRequest.Migrate body
    ) {
        var result = timelineCommandUseCase.migrate(Timeline.Id.from(id), body.validAt(), body.description());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/merge")
    public ResponseEntity<?> merge(
        @PathVariable("id") UUID id,
        @RequestBody TimelineRequest.Merge body
    ) {
        var result = timelineCommandUseCase.merge(Timeline.Id.from(id), body.snapshotIds().stream().map(Snapshot.Id::from).toList(), body.description());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/rollback")
    public ResponseEntity<?> rollback(
            @PathVariable("id") UUID id,
            @RequestBody TimelineRequest.Rollback body
    ) {
        var result = timelineCommandUseCase.rollback(Timeline.Id.from(id), body.targetTxAt(), body.description());
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TimelineResult.Command> update(
            @PathVariable("id") UUID id,
            @RequestBody TimelineRequest.Update body
    ) {
        var result = timelineCommandUseCase.update(Timeline.Id.from(id), body.description());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TimelineResult.Command> delete(
            @PathVariable("id") UUID id
    ) {
        timelineCommandUseCase.delete(Timeline.Id.from(id));
        return ResponseEntity.ok().build();
    }
}
