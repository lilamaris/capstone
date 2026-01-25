package com.lilamaris.capstone.timeline.application.port.in;

import java.util.List;

public interface SlotPathResolver {
    List<SlotPathEntry> getHierarchy(SlotPathResolverOption option);
}
