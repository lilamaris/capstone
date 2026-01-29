package com.lilamaris.capstone.access.resource.application.port.out;

import com.lilamaris.capstone.access.resource.domain.Resource;

import java.util.List;

public interface ResourceStore {
    boolean isExists(ResourceReadOption option);

    List<Resource> get(ResourceReadOption option);

    Resource save(Resource save);
}
