package com.github.sansarch.desafio_totvs.application.auth;

public interface TokenProvider {
    String generateToken(String username);
    String extractUsername(String token);
    String validateToken(String token);
}
