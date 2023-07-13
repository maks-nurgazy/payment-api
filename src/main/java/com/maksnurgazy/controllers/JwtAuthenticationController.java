package com.maksnurgazy.controllers;

import com.maksnurgazy.exception.AuthenticationException;
import com.maksnurgazy.exception.BadRequestException;
import com.maksnurgazy.model.JwtRequest;
import com.maksnurgazy.model.JwtResponse;
import com.maksnurgazy.services.TokenService;
import com.maksnurgazy.services.impl.JwtUserDetailsService;
import com.maksnurgazy.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/v1")
public class JwtAuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService userDetailsService;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final JwtResponse jwtResponse = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader (name="Authorization") String bearerToken){
        tokenService.delete(bearerToken);

        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }


    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new AuthenticationException("User is disabled!");
        } catch (BadCredentialsException e) {
            throw new BadRequestException("Invalid credentials provided!");
        }
    }
}
