package com.lilamaris.capstone.timeline.infrastructure.persistence.jpa.specification;

import com.lilamaris.capstone.snapshot.domain.Snapshot;
import com.lilamaris.capstone.timeline.domain.embed.Effective;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public class TimelineSpecification {
    public static Specification<Snapshot> timelineEqual(TimelineId timelineId) {
        return (root, query, builder) -> builder.equal(root.get("timelineId"), timelineId);
    }

    public static Specification<Snapshot> betweenTx(Instant txAt) {
        return (root, query, builder) -> builder.and(
                builder.lessThanOrEqualTo(root.get("tx").get("from"), txAt),
                builder.greaterThan(root.get("tx").get("to"), txAt)
        );
    }

    public static Specification<Snapshot> isOpenTx() {
        return (root, query, builder) -> builder.and(
                builder.equal(root.get("tx").get("to"), Effective.MAX)
        );
    }

    public static Specification<Snapshot> betweenValid(Instant validAt) {
        return (root, query, builder) -> builder.and(
                builder.lessThanOrEqualTo(root.get("valid").get("from"), validAt),
                builder.greaterThan(root.get("valid").get("to"), validAt)
        );
    }
}
