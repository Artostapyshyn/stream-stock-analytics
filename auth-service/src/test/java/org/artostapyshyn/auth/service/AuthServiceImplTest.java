package org.artostapyshyn.auth.service;

import org.artostapyshyn.auth.jwt.JwtUtil;
import org.artostapyshyn.auth.model.AuthRequest;
import org.artostapyshyn.auth.model.AuthResponse;
import org.artostapyshyn.auth.model.UserVO;
import org.artostapyshyn.auth.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class AuthServiceImplTest {

    private final RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
    private final JwtUtil jwtUtil = Mockito.mock(JwtUtil.class);
    private final AuthServiceImpl authService = new AuthServiceImpl(restTemplate, jwtUtil);

    @Test
    void testRegister() {
        AuthRequest request = new AuthRequest("test@example.com", "password", "Test User");
        UserVO userVO = new UserVO("123", "test@example.com", "hashed", "USER");

        Mockito.when(restTemplate.postForObject(Mockito.anyString(), Mockito.any(), Mockito.eq(UserVO.class)))
                .thenReturn(userVO);
        Mockito.when(jwtUtil.generateToken("123", "USER", "ACCESS")).thenReturn("access-token");
        Mockito.when(jwtUtil.generateToken("123", "USER", "REFRESH")).thenReturn("refresh-token");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertNotNull(response.getAccessToken());
        assertNotNull(response.getRefreshToken());
    }
}

