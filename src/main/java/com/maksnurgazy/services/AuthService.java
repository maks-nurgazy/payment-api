package com.maksnurgazy.services;

import com.maksnurgazy.model.JwtRequest;
import com.maksnurgazy.model.JwtResponse;

public interface AuthService {
    JwtResponse login(JwtRequest jwtRequest);
}
