package com.github.sansarch.desafio_totvs.domain.exception.payable;

public class PayableDueDateRequiredException extends RuntimeException {
    public PayableDueDateRequiredException() {
        super("Due date is required.");
    }
}
