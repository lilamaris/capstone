package com.lilamaris.capstone.identity.auth.persistence;

import com.lilamaris.capstone.identity.auth.persistence.jwks.io.JwksFileProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@AutoConfiguration
@EnableConfigurationProperties(JwksFileProperties.class)
public class IdentityAutoPersistenceAutoConfigure {
}
