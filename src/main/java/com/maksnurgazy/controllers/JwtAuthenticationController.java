package com.maksnurgazy.controllers;

import com.maksnurgazy.entities.Transaction;
import com.maksnurgazy.entities.User;
import com.maksnurgazy.exception.AuthenticationException;
import com.maksnurgazy.exception.NotFoundException;
import com.maksnurgazy.model.JwtRequest;
import com.maksnurgazy.model.JwtResponse;
import com.maksnurgazy.repositories.TransactionRepository;
import com.maksnurgazy.repositories.UserRepository;
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
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/v1")
public class JwtAuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final JwtResponse jwtResponse = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(Authentication authentication){
        return ResponseEntity.ok(null);
    }

    @PostMapping("/payment")
    public ResponseEntity<String> payment(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(()-> new NotFoundException("User not found."));

        // Check if the user has sufficient balance for the payment
        BigDecimal paymentAmount = new BigDecimal("1.1");
        if (user.getBalance().compareTo(paymentAmount) < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient balance");
        }

        // Perform the payment operation
        BigDecimal newBalance = user.getBalance().subtract(paymentAmount);
        user.setBalance(newBalance);
        userRepository.save(user);

        // Record the transaction in the database
        Transaction transaction = new Transaction(user.getId(), paymentAmount, LocalDateTime.now());
        transactionRepository.save(transaction);

        return ResponseEntity.ok("Payment successful");
    }


    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new AuthenticationException("User is disabled!");
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Invalid credentials provided!");
        }
    }
}
