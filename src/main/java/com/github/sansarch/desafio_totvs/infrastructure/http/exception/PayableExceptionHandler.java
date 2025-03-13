package com.github.sansarch.desafio_totvs.infrastructure.http.exception;

import com.github.sansarch.desafio_totvs.domain.exception.payable.PayableAlreadyPaidException;
import com.github.sansarch.desafio_totvs.domain.exception.payable.PayableNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class PayableExceptionHandler {

    @ExceptionHandler(PayableAlreadyPaidException.class)
    public ResponseEntity<ErrorResponse> handlePayableAlreadyPaidException(PayableAlreadyPaidException ex) {
        log.error(ex.getMessage());
        var errorData = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorData);
    }

    @ExceptionHandler(PayableNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePayableNotFoundException(PayableNotFoundException ex) {
        log.error(ex.getMessage());
        var errorData = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorData);
    }
}
