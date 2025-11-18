package com.lilamaris.capstone.application.configuration;

import com.lilamaris.capstone.application.util.DomainTypeRegistry;
import com.lilamaris.capstone.domain.degree_course.Course;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
    @Bean
    public DomainTypeRegistry domainTypeRegistry() {
        DomainTypeRegistry registry = new DomainTypeRegistry();
        registry.register("Course", Course.class);
        return registry;
    }
}
