package com.github.sansarch.desafio_totvs.domain.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PayableTest {

    @Test
    void shouldReturnCorrectId() {
        Payable payable = new Payable(1L, null, null, null, null, null);
        assertEquals(1L, payable.getId());
    }

    @Test
    void shouldReturnNullWhenIdIsNull() {
        Payable payable = new Payable(null, null, null, null, null, null);
        assertNull(payable.getId());
    }

    @Test
    void shouldReturnCorrectDueDate() {
        LocalDateTime dueDate = LocalDateTime.now();
        Payable payable = new Payable(1L, dueDate, null, null, null, null);
        assertEquals(dueDate, payable.getDueDate());
    }

    @Test
    void shouldReturnCorrectPaymentDate() {
        LocalDateTime paymentDate = LocalDateTime.now();
        Payable payable = new Payable(1L, null, paymentDate, null, null, null);
        assertEquals(paymentDate, payable.getPaymentDate());
    }

    @Test
    void shouldReturnCorrectValue() {
        BigDecimal value = BigDecimal.valueOf(100.00);
        Payable payable = new Payable(1L, null, null, value, null, null);
        assertEquals(value, payable.getValue());
    }

    @Test
    void shouldReturnCorrectDescription() {
        String description = "Test Description";
        Payable payable = new Payable(1L, null, null, null, description, null);
        assertEquals(description, payable.getDescription());
    }

    @Test
    void shouldReturnCorrectStatus() {
        var status = PayableStatus.PAID;
        Payable payable = new Payable(1L, null, null, null, null, status);
        assertEquals(status, payable.getStatus());
    }

    @Test
    void shouldCancelPayable() {
        Payable payable = new Payable(1L, null, null, null, null, PayableStatus.PENDING);
        payable.cancel();
        assertEquals(PayableStatus.CANCELED, payable.getStatus());
    }
}