package com.artostapyshyn.api.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("data_retrieval_service_route", r ->
                        r.path("/api/v1/data-retrieval/**")
                                .uri("http://localhost:9001"))
                .build();
    }
}
