package com.lilamaris.capstone.refresh_token.application.config;

import com.lilamaris.capstone.refresh_token.domain.id.RefreshTokenId;
import com.lilamaris.capstone.shared.application.identity.IdGenerator;
import com.lilamaris.capstone.shared.application.identity.RawGenerator;
import com.lilamaris.capstone.shared.application.identity.defaults.RawBasedIdGenerator;
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
