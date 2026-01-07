package com.lilamaris.capstone.shared.application.access_control.config;

import com.lilamaris.capstone.shared.application.access_control.contract.DomainPolicy;
import com.lilamaris.capstone.shared.application.access_control.contract.DomainPolicyDirectory;
import com.lilamaris.capstone.shared.application.access_control.defaults.DefaultPolicyDirectory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AccessControlPolicyConfig {
    @Bean
    public DomainPolicyDirectory domainPolicyDirectory(
            List<DomainPolicy<?>> policies
    ) {
        var dir = new DefaultPolicyDirectory();
        policies.forEach(dir::addPolicy);
        return dir;
    }
}
