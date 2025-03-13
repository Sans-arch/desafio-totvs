package com.github.sansarch.desafio_totvs.domain.exception.payable;

public class PayableValueRequiredException extends RuntimeException {
    public PayableValueRequiredException() {
        super("Value is required.");
    }
}
