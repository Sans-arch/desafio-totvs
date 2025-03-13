package com.github.sansarch.desafio_totvs.infrastructure.http.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private int status;
    private String error;
}
