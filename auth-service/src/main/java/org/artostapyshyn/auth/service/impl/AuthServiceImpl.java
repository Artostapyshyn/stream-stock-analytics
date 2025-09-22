package org.artostapyshyn.auth.service.impl;

import lombok.AllArgsConstructor;
import org.artostapyshyn.auth.jwt.JwtUtil;
import org.artostapyshyn.auth.model.AuthRequest;
import org.artostapyshyn.auth.model.AuthResponse;
import org.artostapyshyn.auth.model.UserVO;
import org.artostapyshyn.auth.service.AuthService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final WebClient webClient;
    private final JwtUtil jwtUtil;

    @Override
    public Mono<AuthResponse> register(AuthRequest request) {
        request.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));

        return webClient.post()
                .uri("http://user-service/api/v1/users")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserVO.class)
                .map(registeredUser -> {
                    String accessToken = jwtUtil.generateToken(
                            registeredUser.getId(),
                            registeredUser.getRole(),
                            "ACCESS"
                    );
                    String refreshToken = jwtUtil.generateToken(
                            registeredUser.getId(),
                            registeredUser.getRole(),
                            "REFRESH"
                    );

                    return new AuthResponse(accessToken, refreshToken);
                });
    }
}
