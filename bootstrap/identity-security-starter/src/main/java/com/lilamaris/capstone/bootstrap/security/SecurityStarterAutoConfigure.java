package com.lilamaris.capstone.bootstrap.security;

import com.lilamaris.capstone.bootstrap.security.customizer.AuthorizeHttpRequestCustomizer;
import com.lilamaris.capstone.bootstrap.security.customizer.HttpSecurityCustomizer;
import com.lilamaris.capstone.bootstrap.security.customizer.OAuth2ResourceServerCustomizer;
import com.lilamaris.capstone.identity.client.IdentityClientAutoConfigure;
import com.lilamaris.capstone.identity.client.jwt.ActorContextBindingFilter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AutoConfiguration(after = IdentityClientAutoConfigure.class)
@EnableWebSecurity
@EnableMethodSecurity
@ConditionalOnClass({HttpSecurity.class, SecurityFilterChain.class, JwtDecoder.class})
@ConditionalOnProperty(
        prefix = "capstone.bootstrap.security",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
@EnableConfigurationProperties(SecurityStarterProperties.class)
public class SecurityStarterAutoConfigure {

    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            ObjectProvider<CorsConfigurationSource> corsSourceProvider,
            List<HttpSecurityCustomizer> httpSecurityCustomizers,
            SecurityStarterProperties properties
    ) throws Exception {
        configureCsrf(http, properties);
        configureCors(http, corsSourceProvider, properties);

        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        var httpCustomizers = new ArrayList<>(httpSecurityCustomizers);
        AnnotationAwareOrderComparator.sort(httpCustomizers);

        for (var customizer : httpCustomizers) customizer.customize(http);

        return http.build();
    }

    @Bean
    @ConditionalOnMissingBean(CorsConfigurationSource.class)
    @ConditionalOnProperty(
            prefix = "capstone.bootstrap.security.cors",
            name = "enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    UrlBasedCorsConfigurationSource corsConfigurationSource(SecurityStarterProperties properties) {
        var cors = properties.cors();

        var configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(cors.allowedOrigins());
        configuration.setAllowedMethods(cors.allowedMethods());
        configuration.setAllowedHeaders(cors.allowedHeaders());
        configuration.setAllowCredentials(cors.allowCredentials());
        configuration.setExposedHeaders(cors.exposedHeaders());
        configuration.validateAllowCredentials();

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean("authorizeHttpRequestsCustomizer")
    @ConditionalOnMissingBean(name = "authorizeHttpRequestsCustomizer")
    HttpSecurityCustomizer authorizeHttpRequestsCustomizer(
            List<AuthorizeHttpRequestCustomizer> authorizeHttpRequestCustomizers,
            SecurityStarterProperties properties
    ) {
        return http -> {
            var customizers = new ArrayList<>(authorizeHttpRequestCustomizers);
            AnnotationAwareOrderComparator.sort(customizers);

            if (!properties.authorize().enabled() && customizers.isEmpty()) {
                return;
            }

            http.authorizeHttpRequests(registry -> {
                for (var customizer : customizers) customizer.customize(registry);
            });
        };
    }

    @Bean("oAuth2ResourceServerCustomizer")
    @ConditionalOnMissingBean(name = "oAuth2ResourceServerCustomizer")
    HttpSecurityCustomizer oAuth2ResourceServerCustomizer(List<OAuth2ResourceServerCustomizer> oAuth2ResourceServerCustomizers) {
        return http -> http.oauth2ResourceServer(oauth2 -> {
            var customizers = new ArrayList<>(oAuth2ResourceServerCustomizers);
            AnnotationAwareOrderComparator.sort(customizers);

            for (var customizer : customizers) customizer.customize(oauth2);
        });
    }

    @Bean
    @ConditionalOnBean(ActorContextBindingFilter.class)
    @ConditionalOnMissingBean(name = "actorContextBindingFilterRegistration")
    FilterRegistrationBean<ActorContextBindingFilter> actorContextBindingFilterRegistration(ActorContextBindingFilter filter) {
        var registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean("actorContextBindingFilterCustomizer")
    @ConditionalOnBean(ActorContextBindingFilter.class)
    @ConditionalOnMissingBean(name = "actorContextBindingFilterCustomizer")
    HttpSecurityCustomizer actorContextBindingFilterCustomizer(ActorContextBindingFilter filter) {
        return http -> http.addFilterAfter(filter, BearerTokenAuthenticationFilter.class);
    }

    @Bean("permitAllRequestsCustomizer")
    @ConditionalOnMissingBean(name = "permitAllRequestsCustomizer")
    @ConditionalOnProperty(
            prefix = "capstone.bootstrap.security.authorize",
            name = "enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    @Order(Ordered.LOWEST_PRECEDENCE)
    AuthorizeHttpRequestCustomizer permitAllRequestCustomizer(SecurityStarterProperties properties) {
        var authorize = properties.authorize();

        return registry -> {
            for (var permit : authorize.permits()) {
                registry.requestMatchers(permit).permitAll();
            }

            switch (authorize.anyRequest()) {
                case AUTHENTICATED -> registry.anyRequest().authenticated();
                case DENY_ALL -> registry.anyRequest().denyAll();
            }
        };
    }

    @Bean("jwtResourceServerCustomizer")
    @ConditionalOnMissingBean(name = "jwtResourceServerCustomizer")
    OAuth2ResourceServerCustomizer jwtResourceServerCustomizer(
            JwtDecoder jwtDecoder,
            Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationTokenConverter
    ) {
        return oauth2 -> oauth2.jwt(jwt -> jwt
                .decoder(jwtDecoder)
                .jwtAuthenticationConverter(jwtAuthenticationTokenConverter)
        );
    }

    private void configureCsrf(HttpSecurity http, SecurityStarterProperties properties) {
        if (properties.csrfEnabled()) http.csrf(Customizer.withDefaults());
        else http.csrf(AbstractHttpConfigurer::disable);
    }

    private void configureCors(HttpSecurity http, ObjectProvider<CorsConfigurationSource> corsSourceProvider, SecurityStarterProperties properties) {
        var cors = properties.cors();
        if (cors.enabled()) {
            var corsSource = Optional.ofNullable(corsSourceProvider.getIfAvailable())
                    .orElseThrow(() -> new IllegalStateException("""
                            CORS is enabled but CorsConfigurationSource bean is not available.
                            Check capstone.bootstrap.security.cors.enabled or define a CorsConfigurationSource bean.
                            """));

            http.cors(configurer -> configurer.configurationSource(corsSource));
        } else {
            http.cors(AbstractHttpConfigurer::disable);
        }
    }
}
