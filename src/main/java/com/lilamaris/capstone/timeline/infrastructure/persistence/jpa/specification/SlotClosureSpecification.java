package com.lilamaris.capstone.timeline.infrastructure.persistence.jpa.specification;

import com.lilamaris.capstone.timeline.domain.SlotClosure;
import com.lilamaris.capstone.timeline.domain.id.SlotId;
import org.springframework.data.jpa.domain.Specification;

public class SlotClosureSpecification {
    public static Specification<SlotClosure> eqDescendantSlotId(SlotId descendantSlotId) {
        return (root, query, cb) -> cb.equal(root.get("descendantSlotId"), descendantSlotId);
    }

    public static Specification<SlotClosure> maxDepth(Integer maxDepth) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("depth"), maxDepth);
    }

    public static Specification<SlotClosure> minDepth(Integer minDepth) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("depth"), minDepth);
    }
}
