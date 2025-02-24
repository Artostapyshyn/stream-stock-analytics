package com.artostapyshyn.auth.service;

import com.artostapyshyn.auth.model.AuthRequest;
import com.artostapyshyn.auth.model.AuthResponse;

public interface AuthService {
    AuthResponse register(AuthRequest request);
}
