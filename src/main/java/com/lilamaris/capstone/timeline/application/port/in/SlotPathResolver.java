package com.lilamaris.capstone.timeline.application.port.in;

import com.lilamaris.capstone.shared.domain.id.ExternalizableId;

import java.util.List;
import java.util.Optional;

public interface SlotPathResolver {
    List<SlotPathEntry> getHierarchy(ExternalizableId slotId);

    Optional<SlotPathEntry> getParent(ExternalizableId slotId);
}
