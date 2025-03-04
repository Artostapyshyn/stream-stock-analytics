package com.artostapyshyn.api.gateway.config;

import com.artostapyshyn.api.gateway.service.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RefreshScope
@AllArgsConstructor
public class AuthenticationFilter implements GatewayFilter {

    private final JwtUtils jwtUtil;

    private final RouterValidator validator;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (validator.isSecured.test(request)) {
            if (authIsMissing(request)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            final String token = request.getHeaders().getOrEmpty("Authorization").getFirst();

            if (!jwtUtil.isExpired(token)) {
                return onError(exchange, HttpStatus.UNAUTHORIZED);
            }

        }
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private boolean authIsMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }
}
