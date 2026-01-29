package com.lilamaris.capstone.bitemporal.delta.application.port.in;

import com.lilamaris.capstone.shared.domain.id.CanonicalExternalId;

import java.util.List;
import java.util.Map;

public interface DeltaReader {
    Map<CanonicalExternalId, List<DeltaEntry>> getDelta(DeltaReadOption option);
}
