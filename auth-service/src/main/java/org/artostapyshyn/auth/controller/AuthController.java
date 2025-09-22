package org.artostapyshyn.auth.controller;

import lombok.AllArgsConstructor;
import org.artostapyshyn.auth.model.AuthRequest;
import org.artostapyshyn.auth.model.AuthResponse;
import org.artostapyshyn.auth.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public Mono<AuthResponse> register(@RequestBody AuthRequest request) {
        return authService.register(request);
    }
}
