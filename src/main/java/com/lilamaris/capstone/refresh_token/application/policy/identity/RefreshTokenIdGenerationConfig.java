package com.lilamaris.capstone.refresh_token.application.policy.identity;

import com.lilamaris.capstone.refresh_token.domain.id.RefreshTokenId;
import com.lilamaris.capstone.shared.application.policy.identity.defaults.RawBasedIdGenerator;
import com.lilamaris.capstone.shared.application.policy.identity.port.in.IdGenerator;
import com.lilamaris.capstone.shared.application.policy.identity.port.in.RawGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RefreshTokenIdGenerationConfig {
    @Bean
    public IdGenerator<RefreshTokenId> refreshTokenIdIdGenerator(@Qualifier("opaque") RawGenerator<String> opaque) {
        return new RawBasedIdGenerator<>(RefreshTokenId.class, RefreshTokenId::new, opaque);
    }
}
