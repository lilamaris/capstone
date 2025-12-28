package com.lilamaris.capstone.domain;

import com.lilamaris.capstone.application.policy.identity.IdGenerationContext;
import com.lilamaris.capstone.application.policy.identity.defaults.DefaultIdGenerationContext;
import com.lilamaris.capstone.application.policy.identity.defaults.RawBasedIdGenerator;
import com.lilamaris.capstone.domain.model.capstone.timeline.Snapshot;
import com.lilamaris.capstone.domain.model.capstone.timeline.SnapshotLink;
import com.lilamaris.capstone.domain.model.capstone.timeline.Timeline;
import com.lilamaris.capstone.domain.model.capstone.timeline.embed.Effective;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.SnapshotLinkId;
import com.lilamaris.capstone.domain.model.capstone.timeline.id.TimelineId;
import com.lilamaris.capstone.util.SequentialUuidGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TimelineTest {
    private static final Logger log = LoggerFactory.getLogger(TimelineTest.class);
    private Instant initialTxAt;
    private Instant initialValidAt;

    private IdGenerationContext ids;

    @BeforeEach
    void run() {
        var timelineIdGen = new RawBasedIdGenerator<>(TimelineId.class, TimelineId::new, new SequentialUuidGenerator());
        var snapshotIdGen = new RawBasedIdGenerator<>(SnapshotId.class, SnapshotId::new, new SequentialUuidGenerator());
        var snapshotLinkIdGen = new RawBasedIdGenerator<>(SnapshotLinkId.class, SnapshotLinkId::new, new SequentialUuidGenerator());
        ids = new DefaultIdGenerationContext(List.of(timelineIdGen, snapshotIdGen, snapshotLinkIdGen));

        initialTxAt = Instant.parse("2025-01-01T00:00:00Z");
        initialValidAt = Instant.parse("2025-01-01T00:00:00Z");
    }

    @Test
    void should_create_initial_snapshot() {
        var initial = Timeline.create("Test Timeline", "Initial", ids.next(TimelineId.class));

        initial.migrate(initialTxAt, initialValidAt, "Test Description", ids.next(SnapshotId.class), ids.next(SnapshotLinkId.class));

        log.info("timeline: {}", initial);

        assertThat(initial).isInstanceOf(Timeline.class);
        assertThat(initial.getSnapshotList()).hasSize(1);
        var tx = Effective.create(initialTxAt, Effective.MAX);
        var valid = Effective.create(initialValidAt, Effective.MAX);

        assertThat(initial.getSnapshotList()).satisfiesExactly(
                s -> {
                    assertThat(s).isInstanceOf(Snapshot.class);
                    assertThat(s.getTx().isOpen()).isTrue();
                    assertThat(s.getValid().isOpen()).isTrue();
                    assertThat(s.getTx()).isEqualTo(tx);
                    assertThat(s.getValid()).isEqualTo(valid);
                }
        );

        var s = initial.getSnapshotList().getFirst();
        assertThat(initial.getSnapshotLinkList()).hasSize(1);
        assertThat(initial.getSnapshotLinkList()).satisfiesExactly(
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
        var initial = Timeline.create("Test Timeline", "Initial", ids.next(TimelineId.class));

        initial.migrate(initialTxAt, initialValidAt, "Test Description", ids.next(SnapshotId.class), ids.next(SnapshotLinkId.class));

        var newTxAt = Instant.parse("2025-02-01T00:00:00Z");
        var newValidAt = Instant.parse("2025-03-01T00:00:00Z");

        initial.migrate(newTxAt, newValidAt, "Migrate #1", ids.next(SnapshotId.class), ids.next(SnapshotLinkId.class));

        assertThat(initial).isInstanceOf(Timeline.class);
        assertThat(initial.getSnapshotList().size()).isEqualTo(3);

        var closed = initial.getSnapshotsWithClosedTx();
        var opened = initial.getSnapshotsWithOpenTx();

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
        var leftSnapshot = opened.stream()
                .filter(s -> s.getValid().equals(leftValid))
                .findFirst()
                .orElseThrow();
        var rightSnapshot = opened.stream()
                .filter(s -> s.getValid().equals(rightValid))
                .findFirst()
                .orElseThrow();

        var linkByDesc = initial.getSnapshotLinkByDescendant();
        var rootLink = linkByDesc.get(closedSnapshot.id());
        var leftLink = linkByDesc.get(leftSnapshot.id());
        var rightLink = linkByDesc.get(rightSnapshot.id());

        assertThat(linkByDesc).hasSize(3);

        assertThat(rootLink.getAncestorSnapshotId()).isNull();
        assertThat(rootLink.getDescendantSnapshotId()).isEqualTo(closedSnapshot.id());

        assertThat(leftLink.getAncestorSnapshotId()).isEqualTo(closedSnapshot.id());
        assertThat(leftLink.getDescendantSnapshotId()).isEqualTo(leftSnapshot.id());

        assertThat(rightLink.getAncestorSnapshotId()).isEqualTo(leftSnapshot.id());
        assertThat(rightLink.getDescendantSnapshotId()).isEqualTo(rightSnapshot.id());

        var linkByAncestor = initial.getSnapshotLinkByAncestor();

        assertThat(linkByAncestor).containsKey(closedSnapshot.id());
        assertThat(linkByAncestor.get(closedSnapshot.id()).getDescendantSnapshotId())
                .isEqualTo(leftSnapshot.id());

        assertThat(linkByAncestor).containsKey(leftSnapshot.id());
        assertThat(linkByAncestor.get(leftSnapshot.id()).getDescendantSnapshotId())
                .isEqualTo(rightSnapshot.id());

        assertThat(linkByAncestor).doesNotContainKey(rightSnapshot.id());
    }
}
