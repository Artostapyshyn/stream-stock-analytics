package org.artostapyshyn.auth.service;


import org.artostapyshyn.auth.model.AuthRequest;
import org.artostapyshyn.auth.model.AuthResponse;
import reactor.core.publisher.Mono;

public interface AuthService {
    Mono<AuthResponse> register(AuthRequest request);
}
