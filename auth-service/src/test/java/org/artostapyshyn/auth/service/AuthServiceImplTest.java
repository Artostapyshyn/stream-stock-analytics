package org.artostapyshyn.auth.service;

import org.artostapyshyn.auth.jwt.JwtUtil;
import org.artostapyshyn.auth.model.AuthRequest;
import org.artostapyshyn.auth.model.AuthResponse;
import org.artostapyshyn.auth.model.UserVO;
import org.artostapyshyn.auth.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AuthServiceImplTest {

    private final WebClient webClient = WebClient.create();
    private final JwtUtil jwtUtil = Mockito.mock(JwtUtil.class);
    private final AuthServiceImpl authService = new AuthServiceImpl(webClient, jwtUtil);

    @Test
    void testRegister() {
        AuthRequest request = new AuthRequest("test@example.com", "password", "Test User");
        UserVO userVO = new UserVO("123", "test@example.com", "hashed", "USER");

        Mockito.when(webClient.post().uri(Mockito.anyString()).body(Mockito.any(), Mockito.eq(UserVO.class)))
                .thenReturn(Mono.just());
        Mockito.when(jwtUtil.generateToken("123", "USER", "ACCESS")).thenReturn("access-token");
        Mockito.when(jwtUtil.generateToken("123", "USER", "REFRESH")).thenReturn("refresh-token");

        AuthResponse response = authService.register(request).block();

        assertNotNull(response);
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());
    }
}
