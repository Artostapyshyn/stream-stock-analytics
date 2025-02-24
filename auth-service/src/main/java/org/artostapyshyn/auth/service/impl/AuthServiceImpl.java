package com.artostapyshyn.auth.service.impl;

import lombok.AllArgsConstructor;
import com.artostapyshyn.auth.jwt.JwtUtil;
import com.artostapyshyn.auth.model.AuthRequest;
import com.artostapyshyn.auth.model.AuthResponse;
import com.artostapyshyn.auth.model.UserVO;
import com.artostapyshyn.auth.service.AuthService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse register(AuthRequest request) {
        request.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        UserVO registeredUser = restTemplate.postForObject("http://user-service/api/v1/users", request, UserVO.class);

        String accessToken = jwtUtil.generateToken(registeredUser.getId(), registeredUser.getRole(), "ACCESS");
        String refreshToken = jwtUtil.generateToken(registeredUser.getId(), registeredUser.getRole(), "REFRESH");

        return new AuthResponse(accessToken, refreshToken);
    }
}
