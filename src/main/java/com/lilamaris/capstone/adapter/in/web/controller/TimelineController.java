package com.lilamaris.capstone.adapter.in.web.controller;

import com.lilamaris.capstone.adapter.in.web.request.TimelineRequest;
import com.lilamaris.capstone.application.port.in.TimelineCommandUseCase;
import com.lilamaris.capstone.application.port.in.TimelineQueryUseCase;
import com.lilamaris.capstone.application.port.in.condition.SnapshotQueryCondition;
import com.lilamaris.capstone.application.port.in.result.TimelineResult;
import com.lilamaris.capstone.domain.timeline.Timeline;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/timeline")
@RequiredArgsConstructor
public class TimelineController {
    private final TimelineCommandUseCase timelineCommandUseCase;
    private final TimelineQueryUseCase timelineQueryUseCase;

    @GetMapping
    public ResponseEntity<?> getAll(

    ) {
        var result = timelineQueryUseCase.getAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCompressedById(
        @PathVariable("id") UUID id
    ) {
        var result = timelineQueryUseCase.getCompressedById(Timeline.Id.from(id));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}/snapshots")
    public ResponseEntity<?> getSnapshots(
            @PathVariable("id") UUID id,
            @RequestParam(required = false) LocalDateTime txAt,
            @RequestParam(required = false) LocalDateTime validAt
    ) {
        var condition = SnapshotQueryCondition.builder().timelineId(Timeline.Id.from(id)).txAt(txAt).validAt(validAt).build();
        var result = timelineQueryUseCase.getSnapshot(condition);
        return ResponseEntity.ok(result);
    }

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
        var result = timelineCommandUseCase.merge(Timeline.Id.from(id), body.validFrom(), body.validTo(), body.description());
        return ResponseEntity.ok(result);
    }

//    @PostMapping("/{id}/rollbackSnapshot")
//    public ResponseEntity<?> rollback(
//            @PathVariable("id") UUID id,
//            @RequestBody TimelineRequest.Rollback body
//    ) {
//        var result = timelineCommandUseCase.rollback(Timeline.Id.from(id), body.targetTxAt(), body.description());
//        return ResponseEntity.ok(result);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<TimelineResult.Command> update(
            @PathVariable("id") UUID id,
            @RequestBody TimelineRequest.Update body
    ) {
        var result = timelineCommandUseCase.update(Timeline.Id.from(id), body.description());
        return ResponseEntity.ok(result);
    }
}
