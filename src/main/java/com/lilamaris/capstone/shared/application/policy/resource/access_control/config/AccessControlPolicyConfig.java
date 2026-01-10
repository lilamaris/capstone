package com.lilamaris.capstone.shared.application.policy.resource.access_control.config;

import com.lilamaris.capstone.shared.application.policy.resource.access_control.defaults.DefaultResourceAccessPolicyDirectory;
import com.lilamaris.capstone.shared.application.policy.resource.access_control.port.in.ResourceAccessPolicy;
import com.lilamaris.capstone.shared.application.policy.resource.access_control.port.in.ResourceAccessPolicyDirectory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AccessControlPolicyConfig {
    @Bean
    public ResourceAccessPolicyDirectory domainPolicyDirectory(
            List<ResourceAccessPolicy> policies
    ) {
        var dir = new DefaultResourceAccessPolicyDirectory();
        policies.forEach(dir::addPolicy);
        return dir;
    }
}
