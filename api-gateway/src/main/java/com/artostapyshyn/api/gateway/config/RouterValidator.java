package com.artostapyshyn.api.gateway.config;

import lombok.AllArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RouterValidator {

    public static final List<String> openEndpoints = List.of(
            "/auth/register"
    );

    static final Predicate<ServerHttpRequest> isSecured =
            request -> openEndpoints.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
