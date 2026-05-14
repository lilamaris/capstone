package com.lilamaris.capstone.bootstrap.application;

import com.lilamaris.capstone.identity.core.actor.context.ActorContextHolder;
import com.lilamaris.capstone.identity.core.actor.context.ThreadLocalActorContextHolder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.time.Clock;

@AutoConfiguration
@ConditionalOnProperty(
        prefix = "capstone.bootstrap.application",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
@EnableConfigurationProperties(ApplicationStarterProperties.class)
public class ApplicationStarterAutoConfigure {

    @Bean
    @ConditionalOnMissingBean
    Clock clock(ApplicationStarterProperties properties) {
        return Clock.system(properties.timezone());
    }

    @Bean
    @ConditionalOnMissingBean(ActorContextHolder.class)
    ActorContextHolder actorContextHolder() {
        return new ThreadLocalActorContextHolder();
    }
}
