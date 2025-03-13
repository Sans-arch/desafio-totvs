package com.github.sansarch.desafio_totvs.domain.exception.payable;

public class PayableDescriptionRequiredException extends RuntimeException {
    public PayableDescriptionRequiredException() {
        super("Description is required.");
    }
}
