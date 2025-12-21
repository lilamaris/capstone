package com.lilamaris.capstone.domain.degree_timeline;

import com.lilamaris.capstone.domain.model.capstone.timeline.Snapshot;
import com.lilamaris.capstone.domain.model.capstone.timeline.SnapshotLink;
import com.lilamaris.capstone.domain.model.capstone.timeline.Timeline;
import com.lilamaris.capstone.domain.model.capstone.timeline.embed.Effective;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

public class TimelineTest {
    private static final Logger log = LoggerFactory.getLogger(TimelineTest.class);
    private Instant initialTxAt;
    private Instant initialValidAt;
    private Timeline t;

    @BeforeEach
    void run() {
        initialTxAt = Instant.parse("2025-01-01T00:00:00Z");
        initialValidAt = Instant.parse("2025-01-01T00:00:00Z");

        t = Timeline.create();
    }

    @Test
    void should_create_initial_snapshot() {
        t.migrate(initialTxAt, initialValidAt, "Test Description");

        log.info("timeline: {}", t);

        assertThat(t).isInstanceOf(Timeline.class);
        assertThat(t.getSnapshotList()).hasSize(1);
        var tx = Effective.create(initialTxAt, Effective.MAX);
        var valid = Effective.create(initialValidAt, Effective.MAX);

        assertThat(t.getSnapshotList()).satisfiesExactly(
                s -> {
                    assertThat(s).isInstanceOf(Snapshot.class);
                    assertThat(s.getTx().isOpen()).isTrue();
                    assertThat(s.getValid().isOpen()).isTrue();
                    assertThat(s.getTx()).isEqualTo(tx);
                    assertThat(s.getValid()).isEqualTo(valid);
                }
        );

        var s = t.getSnapshotList().getFirst();
        assertThat(t.getSnapshotLinkList()).hasSize(1);
        assertThat(t.getSnapshotLinkList()).satisfiesExactly(
                l -> {
                    assertThat(l).isInstanceOf(SnapshotLink.class);
                    assertThat(l.isRoot()).isTrue();
                    assertThat(l.getAncestorSnapshotId()).isNull();
                    assertThat(l.getDescendantSnapshotId()).isEqualTo(s.id());
                }
        );
    }

    @Test
    void should_migrate() {
        t.migrate(initialTxAt, initialValidAt, "Test Description");

        var newTxAt = Instant.parse("2025-02-01T00:00:00Z");
        var newValidAt = Instant.parse("2025-03-01T00:00:00Z");

        t.migrate(newTxAt, newValidAt, "Migrate #1");

        assertThat(t).isInstanceOf(Timeline.class);
        assertThat(t.getSnapshotList().size()).isEqualTo(3);

        var closed = t.getSnapshotsWithClosedTx();
        var opened = t.getSnapshotsWithOpenTx();

        // start validate snapshot invariants
        var closedTx = Effective.create(initialTxAt, newTxAt);
        assertThat(closed).hasSize(1);
        assertThat(closed.getFirst().getTx()).isEqualTo(closedTx);

        var openedTx = Effective.create(newTxAt, Effective.MAX);
        var leftValid = Effective.create(initialValidAt, newValidAt);
        var rightValid = Effective.create(newValidAt, Effective.MAX);
        assertThat(opened).hasSize(2);
        assertThat(opened).satisfiesExactly(
                sl -> {
                    assertThat(sl.getTx()).isEqualTo(openedTx);
                    assertThat(sl.getValid()).isEqualTo(leftValid);
                },
                sr -> {
                    assertThat(sr.getTx()).isEqualTo(openedTx);
                    assertThat(sr.getValid()).isEqualTo(rightValid);
                }
        );

        // start validate snapshot link invariants
        var closedSnapshot = closed.getFirst();
        var newSnapshotLeft = opened.getFirst();
        var newSnapshotRight = opened.getLast();

        assertThat(t.getSnapshotLinkByDescendant())
                .hasEntrySatisfying(closedSnapshot.id(), l -> {
                    assertThat(l.getAncestorSnapshotId()).isNull();
                })
                .hasEntrySatisfying(newSnapshotLeft.id(), l -> {
                    assertThat(l.getAncestorSnapshotId()).isEqualTo(closedSnapshot.id());
                })
                .hasEntrySatisfying(newSnapshotRight.id(), l -> {
                    assertThat(l.getAncestorSnapshotId()).isEqualTo(newSnapshotLeft.id());
                });

        assertThat(t.getSnapshotLinkByAncestor())
                .hasEntrySatisfying(closedSnapshot.id(), l -> {
                    assertThat(l.getAncestorSnapshotId()).isEqualTo(closedSnapshot.id());
                    assertThat(l.getDescendantSnapshotId()).isEqualTo(newSnapshotLeft.id());
                })
                .hasEntrySatisfying(newSnapshotLeft.id(), l -> {
                    assertThat(l.getAncestorSnapshotId()).isEqualTo(newSnapshotLeft.id());
                    assertThat(l.getDescendantSnapshotId()).isEqualTo(newSnapshotRight.id());
                });

        log.info("timeline: {}", t);
    }
}
