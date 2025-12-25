package com.lilamaris.capstone.domain.model.capstone.user;

import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;
import com.lilamaris.capstone.domain.model.common.id.IdGenerationContext;
import com.lilamaris.capstone.domain.model.common.id.RawGenerator;
import com.lilamaris.capstone.application.util.generator.DefaultIdGenerationContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserFactory {
    private final IdGenerationContext idGenerationContext;

    public UserFactory(
            RawGenerator<UUID> uuidRawGenerator
    ) {
        Map<Class<?>, DefaultIdGenerationContext.Binding<?, ?>> map = Map.of(
                UserId.class, DefaultIdGenerationContext.bind(UserId::new, uuidRawGenerator)
        );

        this.idGenerationContext = new DefaultIdGenerationContext(map);
    }

    public User create(String displayName, Role role) {
        var id = idGenerationContext.next(UserId.class);
        return new User(idGenerationContext, id, displayName, role);
    }
}
