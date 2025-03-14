package com.github.sansarch.desafio_totvs.infrastructure.http.dto.auth;

public record AuthenticationRequestDto(
        String username,
        String password
) {
}
