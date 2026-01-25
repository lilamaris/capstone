package com.lilamaris.capstone.timeline.application.port.in;

import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import org.springframework.lang.Nullable;

public record SlotPathResolverOption(
        ExternalizableId targetSlotId,
        @Nullable Integer minDepth,
        @Nullable Integer maxDepth
) {
    public static SlotPathResolverOption between(ExternalizableId targetSlotId, Integer minDepth, Integer maxDepth) {
        return new SlotPathResolverOption(
                targetSlotId,
                minDepth,
                maxDepth
        );
    }

    public static SlotPathResolverOption distanceOf(ExternalizableId targetSlotId, Integer distance) {
        return new SlotPathResolverOption(
                targetSlotId,
                distance,
                distance + 1
        );
    }

    public static SlotPathResolverOption max(ExternalizableId targetSlotId, Integer maxDepth) {
        return new SlotPathResolverOption(
                targetSlotId,
                null,
                maxDepth
        );
    }

    public static SlotPathResolverOption min(ExternalizableId targetSlotId, Integer minDepth) {
        return new SlotPathResolverOption(
                targetSlotId,
                minDepth,
                null
        );
    }

    public static SlotPathResolverOption self(ExternalizableId targetSlotId) {
        return new SlotPathResolverOption(
                targetSlotId,
                0,
                1
        );
    }

    public static SlotPathResolverOption all(ExternalizableId targetSlotId) {
        return new SlotPathResolverOption(
                targetSlotId,
                null,
                null
        );
    }

    public boolean isSelf() {
        return maxDepth == null && minDepth == null;
    }

    public boolean hasMinLimit() {
        return minDepth != null;
    }

    public boolean hasMaxLimit() {
        return maxDepth != null;
    }
}
