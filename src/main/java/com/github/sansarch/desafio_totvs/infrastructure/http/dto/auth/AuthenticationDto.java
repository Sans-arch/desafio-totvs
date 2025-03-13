package com.github.sansarch.desafio_totvs.infrastructure.http.dto.auth;

public record AuthenticationDto(
        String username,
        String password
) {
}
