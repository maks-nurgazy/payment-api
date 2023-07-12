package com.maksnurgazy.services.impl;


import com.maksnurgazy.entities.Token;
import com.maksnurgazy.repositories.TokenRepository;
import com.maksnurgazy.services.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository repository;

    public Optional<Token> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public void save(Token token) {
        repository.save(token);
    }
}
