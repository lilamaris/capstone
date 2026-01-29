package com.lilamaris.capstone.resource.application.port.out;

import com.lilamaris.capstone.resource.domain.Resource;

import java.util.List;

public interface ResourceStore {
    boolean isExists(ResourceReadOption option);

    List<Resource> get(ResourceReadOption option);

    Resource save(Resource save);
}
