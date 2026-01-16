package com.lilamaris.capstone.timeline.infrastructure.persistence.jpa.specification;

import com.lilamaris.capstone.timeline.domain.Slot;
import com.lilamaris.capstone.timeline.domain.embed.Effective;
import com.lilamaris.capstone.timeline.domain.id.TimelineId;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public class SnapshotSlotSpecification {
    public static Specification<Slot> timelineEqual(TimelineId timelineId) {
        return (root, query, builder) -> builder.equal(root.get("timelineId"), timelineId);
    }

    public static Specification<Slot> betweenTx(Instant txAt) {
        return (root, query, builder) -> builder.and(
                builder.lessThanOrEqualTo(root.get("tx").get("from"), txAt),
                builder.greaterThan(root.get("tx").get("to"), txAt)
        );
    }

    public static Specification<Slot> isOpenTx() {
        return (root, query, builder) -> builder.and(
                builder.equal(root.get("tx").get("to"), Effective.MAX)
        );
    }

    public static Specification<Slot> betweenValid(Instant validAt) {
        return (root, query, builder) -> builder.and(
                builder.lessThanOrEqualTo(root.get("valid").get("from"), validAt),
                builder.greaterThan(root.get("valid").get("to"), validAt)
        );
    }
}
