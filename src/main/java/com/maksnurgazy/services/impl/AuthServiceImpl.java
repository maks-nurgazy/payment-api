package com.maksnurgazy.services.impl;

import com.maksnurgazy.entities.User;
import com.maksnurgazy.exception.AuthenticationException;
import com.maksnurgazy.exception.BadRequestException;
import com.maksnurgazy.model.JwtRequest;
import com.maksnurgazy.model.JwtResponse;
import com.maksnurgazy.repositories.UserRepository;
import com.maksnurgazy.services.AuthService;
import com.maksnurgazy.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService userDetailsService;

    private static final int MAX_FAILED_LOGIN_ATTEMPTS = 3;
    private static final int LOCKOUT_DURATION_MINUTES =60*24;

    @Override
    public JwtResponse login(JwtRequest jwtRequest) {
        String username = jwtRequest.getUsername();
        String password = jwtRequest.getPassword();

        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        User user = (User) userDetails;

        if (user.getLockoutTime() != null && user.getLockoutTime().isAfter(LocalDateTime.now())) {
            throw new AuthenticationException("Account is locked");
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new AuthenticationException("User is disabled!");
        } catch (BadCredentialsException e) {
            incrementFailedLoginAttempts(user);
            throw new BadRequestException("Invalid credentials provided!");
        }

        resetFailedLoginAttempts(user);

        return jwtTokenUtil.generateToken(userDetails);
    }

    private void incrementFailedLoginAttempts(User user) {
        if (user != null) {
            int attempts = user.getFailedLoginAttempts() + 1;
            user.setFailedLoginAttempts(attempts);
            if (attempts >= MAX_FAILED_LOGIN_ATTEMPTS) {
                user.setLockoutTime(LocalDateTime.now().plusMinutes(LOCKOUT_DURATION_MINUTES));
            }
            userRepository.save(user);
        }
    }

    private void resetFailedLoginAttempts(User user) {
        if (user != null && user.getFailedLoginAttempts() > 0) {
            user.setFailedLoginAttempts(0);
            user.setLockoutTime(null);
            userRepository.save(user);
        }
    }
}
