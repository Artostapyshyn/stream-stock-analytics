package com.artostapyshyn.api.gateway.config;

import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableHystrix
@AllArgsConstructor
public class GatewayConfig {

    private final AuthenticationFilter filter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", r -> r.path("/api/v1/users/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://user-service"))
                .route("auth-service", r -> r.path("/api/v1/auth/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://auth-service"))
                .route("data-retrieval-service-route", r -> r.path("/api/v1/data-retrieval/**")
                                .uri("lb://data-retrieval-service"))
                .route("data-analysis-service-route", r -> r.path("/api/v1/data-analysis/**")
                                .uri("lb://data-analysis-service"))
                .route("report-service-route", r -> r.path("/api/v1/report/**")
                                .uri("lb://data-analysis-service"))
                .build();
    }
}

