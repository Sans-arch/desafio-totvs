package com.github.sansarch.desafio_totvs.domain.exception.payable;

public class PayableNotFoundException extends RuntimeException {
    public PayableNotFoundException(String message) {
        super(message);
    }
}
