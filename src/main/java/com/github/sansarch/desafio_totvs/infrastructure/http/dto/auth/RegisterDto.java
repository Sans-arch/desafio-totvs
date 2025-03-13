package com.github.sansarch.desafio_totvs.infrastructure.http.dto.auth;

public record RegisterDto(
        String username,
        String password
) {
}
