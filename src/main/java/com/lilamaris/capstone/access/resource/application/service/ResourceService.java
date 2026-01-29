package com.lilamaris.capstone.access.resource.application.service;

import com.lilamaris.capstone.access.resource.application.port.in.ResourceReader;
import com.lilamaris.capstone.access.resource.application.port.in.ResourceRegister;
import com.lilamaris.capstone.access.resource.application.port.out.ResourceReadOption;
import com.lilamaris.capstone.access.resource.application.port.out.ResourceStore;
import com.lilamaris.capstone.access.resource.domain.Resource;
import com.lilamaris.capstone.access.resource.domain.id.ResourceId;
import com.lilamaris.capstone.shared.application.policy.domain.identity.port.in.IdGenerationDirectory;
import com.lilamaris.capstone.shared.domain.id.DomainRef;
import com.lilamaris.capstone.shared.domain.id.ExternalizableId;
import com.lilamaris.capstone.shared.domain.type.DomainType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceService implements
        ResourceReader,
        ResourceRegister {
    private final ResourceStore resourceStore;
    private final IdGenerationDirectory ids;

    @Override
    public boolean isExists(DomainType type, ExternalizableId id) {
        var option = new ResourceReadOption(type, id);
        return resourceStore.isExists(option);
    }

    @Override
    public List<ExternalizableId> getByType(DomainType type) {
        var option = ResourceReadOption.typeOnly(type);
        return resourceStore.get(option).stream()
                .map(Resource::getResourceId)
                .toList();
    }

    @Override
    public DomainRef register(DomainType type, ExternalizableId id) {
        var resource = Resource.create(
                ids.next(ResourceId.class),
                type,
                id
        );
        var saved = resourceStore.save(resource);
        return saved.getResource();
    }
}
