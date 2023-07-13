package com.maksnurgazy.services;


import com.maksnurgazy.entities.Token;

import java.util.Optional;

public interface TokenService {
    Optional<Token> findById(String k);

    void save(Token token);

    void delete(String token);

    boolean isTokenExists(String token);
}
