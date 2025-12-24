package com.lilamaris.capstone.domain.model.capstone.user;

import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;
import com.lilamaris.capstone.domain.model.common.id.IdGenerationContext;
import com.lilamaris.capstone.domain.model.common.id.IdGenerator;
import com.lilamaris.capstone.domain.model.common.id.IdSpec;
import com.lilamaris.capstone.domain.model.common.id.RawGenerator;
import com.lilamaris.capstone.domain.model.common.id.impl.DefaultIdGenerateContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserFactory {
    private final IdGenerationContext idGenerationContext;

    public UserFactory(
            IdGenerator idGenerator,
            RawGenerator<UUID> uuidRawGenerator
    ) {
        Map<IdSpec<?, ?>, RawGenerator<?>> map = Map.of(
                UserId.SPEC, uuidRawGenerator
        );

        this.idGenerationContext = new DefaultIdGenerateContext(idGenerator, map);
    }

    public User create(String displayName, Role role) {
        var id = idGenerationContext.next(UserId.SPEC);
        return new User(id, displayName, role);
    }
}
