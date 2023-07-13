package com.maksnurgazy.services.impl;


import com.maksnurgazy.entities.Token;
import com.maksnurgazy.repositories.TokenRepository;
import com.maksnurgazy.services.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;
    public static final String BEARER = "Bearer ";

    public Optional<Token> findById(String id) {
        return tokenRepository.findById(id);
    }

    @Override
    public void save(Token token) {
        tokenRepository.save(token);
    }

    @Override
    public void delete(String bearerToken){
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
            tokenRepository.deleteById(bearerToken.substring(7));
        }
    }

    @Override
    public boolean isTokenExists(String token) {
        return findById(token).isPresent();
    }
}
