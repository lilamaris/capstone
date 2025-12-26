package com.lilamaris.capstone.domain.model.auth.refreshToken;

import com.lilamaris.capstone.application.util.generator.DefaultIdGenerationContext;
import com.lilamaris.capstone.domain.model.auth.refreshToken.id.RefreshTokenId;
import com.lilamaris.capstone.domain.model.capstone.user.id.UserId;
import com.lilamaris.capstone.domain.model.common.id.IdGenerationContext;
import com.lilamaris.capstone.domain.model.common.id.RawGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RefreshTokenFactory {
    private final IdGenerationContext idGenerationContext;

    public RefreshTokenFactory(
            @Qualifier("opaqueToken") RawGenerator<String> stringRawGenerator
    ) {
        Map<Class<?>, DefaultIdGenerationContext.Binding<?, ?>> map = Map.of(
                RefreshTokenId.class, DefaultIdGenerationContext.bind(RefreshTokenId::new, stringRawGenerator)
        );

        this.idGenerationContext = new DefaultIdGenerationContext(map);
    }

    public RefreshToken create(UserId userId) {
        var id = idGenerationContext.next(RefreshTokenId.class);
        return new RefreshToken(id, userId);
    }
}
