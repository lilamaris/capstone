package com.lilamaris.capstone.domain.model.auth.refreshToken;

import com.lilamaris.capstone.domain.model.auth.refreshToken.id.RefreshTokenId;
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
public class RefreshTokenFactory {
    private final IdGenerationContext idGenerationContext;

    public RefreshTokenFactory(
            IdGenerator idGenerator,
            RawGenerator<UUID> uuidRawGenerator
    ) {
        Map<IdSpec<?, ?>, RawGenerator<?>> map = Map.of(
                RefreshTokenId.SPEC, uuidRawGenerator
        );

        this.idGenerationContext = new DefaultIdGenerateContext(idGenerator, map);
    }

    public RefreshToken create(UserId userId) {
        var id = idGenerationContext.next(RefreshTokenId.SPEC);
        return new RefreshToken(id, userId);
    }
}
