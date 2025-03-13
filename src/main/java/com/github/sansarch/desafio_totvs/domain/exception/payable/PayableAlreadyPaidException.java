package com.github.sansarch.desafio_totvs.domain.exception.payable;

public class PayableAlreadyPaidException extends RuntimeException {
    public PayableAlreadyPaidException(String message) {
        super(message);
    }
}
