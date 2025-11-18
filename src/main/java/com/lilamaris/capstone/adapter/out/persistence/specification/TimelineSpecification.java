package com.lilamaris.capstone.adapter.out.persistence.specification;

import com.lilamaris.capstone.adapter.out.persistence.entity.SnapshotEntity;
import com.lilamaris.capstone.domain.embed.Effective;
import com.lilamaris.capstone.domain.timeline.Timeline;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public class TimelineSpecification {
    public static Specification<SnapshotEntity> timelineEqual(Timeline.Id timelineId) {
        return (root, query, builder) -> builder.equal(root.get("timelineId"), timelineId.value());
    }

    public static Specification<SnapshotEntity> betweenTx(Instant txAt) {
        return (root, query, builder) -> builder.and(
                builder.lessThanOrEqualTo(root.get("tx").get("from"), txAt),
                builder.greaterThan(root.get("tx").get("to"), txAt)
        );
    }

    public static Specification<SnapshotEntity> isOpenTx() {
        return (root, query, builder) ->  builder.and(
                builder.equal(root.get("tx").get("to"), Effective.MAX)
        );
    }

    public static Specification<SnapshotEntity> betweenValid(Instant validAt) {
        return (root, query, builder) -> builder.and(
                builder.lessThanOrEqualTo(root.get("valid").get("from"), validAt),
                builder.greaterThan(root.get("valid").get("to"), validAt)
        );
    }
}
