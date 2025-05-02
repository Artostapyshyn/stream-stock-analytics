package org.artostapyshyn.auth.service;


import org.artostapyshyn.auth.model.AuthRequest;
import org.artostapyshyn.auth.model.AuthResponse;

public interface AuthService {
    AuthResponse register(AuthRequest request);
}
