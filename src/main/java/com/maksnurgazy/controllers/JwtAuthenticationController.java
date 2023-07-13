package com.maksnurgazy.controllers;

import com.maksnurgazy.model.JwtRequest;
import com.maksnurgazy.services.AuthService;
import com.maksnurgazy.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/v1")
public class JwtAuthenticationController {
    private final TokenService tokenService;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest jwtRequest) {
        return ResponseEntity.ok(authService.login(jwtRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader (name="Authorization") String bearerToken){
        tokenService.delete(bearerToken);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
