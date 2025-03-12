package com.github.sansarch.desafio_totvs.domain.entity;

import com.github.sansarch.desafio_totvs.domain.exception.PayableException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class Payable {

    private Long id;
    private LocalDateTime dueDate;
    private LocalDateTime paymentDate;
    private BigDecimal value;
    private String description;
    private PayableStatus status;

    public void markAsPaid() {
        if (this.status != PayableStatus.PENDING) {
            throw new PayableException("The payable was already paid or canceled.");
        }
        this.paymentDate = LocalDateTime.now();
        this.status = PayableStatus.PAID;
    }

    public void cancel() {
        this.status = PayableStatus.CANCELED;
    }

    public boolean isOverdue() {
        return LocalDateTime.now().isAfter(dueDate) && status == PayableStatus.PENDING;
    }
}
