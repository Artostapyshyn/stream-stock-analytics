package org.artostapyshyn.auth.controller;

import org.artostapyshyn.auth.model.AuthRequest;
import org.artostapyshyn.auth.model.AuthResponse;
import org.artostapyshyn.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AuthControllerTest {

    private final AuthService authService = Mockito.mock(AuthService.class);
    private final AuthController authController = new AuthController(authService);

    @Test
    void testRegister() {
        AuthRequest request = new AuthRequest("test@example.com", "password", "Test User");
        AuthResponse response = new AuthResponse("access-token", "refresh-token");

        Mockito.when(authService.register(request)).thenReturn(Mono.just(response));
        Mono<AuthResponse> result = authController.register(request);
        assertEquals(response, result.block());
    }
}

