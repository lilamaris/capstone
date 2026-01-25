package com.lilamaris.capstone.delta.application.port.in;

import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.type.DomainType;
import org.springframework.lang.Nullable;

import java.util.List;

public record DeltaReadOption(
        List<ExternalizableId> slotIds,
        @Nullable DomainType resourceType,
        @Nullable ExternalizableId resourceId
) {
    public DeltaReadOption {
        if (slotIds == null || slotIds.isEmpty()) {
            throw new IllegalArgumentException(
                    "Slot ids cannot be null or empty."
            );
        }
        if (resourceId != null && resourceType == null) {
            throw new IllegalArgumentException(
                    "Can not create read option that specify only resource id without specifying the resource type."
            );
        }
    }

    public static DeltaReadOption idAndType(List<ExternalizableId> slotIds, DomainType resourceType, ExternalizableId resourceId) {
        return new DeltaReadOption(
                slotIds,
                resourceType,
                resourceId
        );
    }

    public static DeltaReadOption typeOnly(List<ExternalizableId> slotIds, DomainType resourceType) {
        return new DeltaReadOption(
                slotIds,
                resourceType,
                null
        );
    }

    public static DeltaReadOption allResource(List<ExternalizableId> slotIds) {
        return new DeltaReadOption(
                slotIds,
                null,
                null
        );
    }

    public static DeltaReadOption idAndType(ExternalizableId slotId, DomainType resourceType, ExternalizableId resourceId) {
        return idAndType(
                List.of(slotId),
                resourceType,
                resourceId
        );
    }

    public static DeltaReadOption typeOnly(ExternalizableId slotId, DomainType resourceType) {
        return typeOnly(
                List.of(slotId),
                resourceType
        );
    }

    public static DeltaReadOption allResource(ExternalizableId slotId) {
        return allResource(
                List.of(slotId)
        );
    }

    public boolean hasResourceType() {
        return resourceType != null;
    }

    public boolean hasResourceId() {
        return resourceId != null;
    }
}
