package com.lilamaris.capstone.domain.configuration;

import com.lilamaris.capstone.domain.degree.Course;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainRegistry {

    @Bean
    public DomainTypeRegistry domainTypeRegistry() {
        DomainTypeRegistry registry = new DomainTypeRegistry();
        registry.register("Course", Course.class);
        return registry;
    }
}
