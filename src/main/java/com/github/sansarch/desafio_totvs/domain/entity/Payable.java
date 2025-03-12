package com.github.sansarch.desafio_totvs.domain.entity;

import com.github.sansarch.desafio_totvs.domain.exception.PayableException;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Payable {

    private UUID id;
    private LocalDateTime dueDate;
    private LocalDateTime paymentDate;
    private BigDecimal value;
    private String description;
    private PayableStatus status;

    public Payable(UUID id, LocalDateTime dueDate, LocalDateTime paymentDate, BigDecimal value, String description, PayableStatus status) {
        this.id = id;
        this.dueDate = dueDate;
        this.paymentDate = paymentDate;
        this.value = value;
        this.description = description;
        this.status = status;
        validate();
    }

    private void validate() {
        if (this.dueDate == null) {
            throw new PayableException("Due date is required.");
        }
        if (this.value == null) {
            throw new PayableException("Value is required.");
        }
        if (this.description == null || this.description.isBlank()) {
            throw new PayableException("Description is required.");
        }
    }

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

    public void changeDueDate(LocalDateTime dueDate) {
        if (dueDate == null) {
            throw new PayableException("Due date is required.");
        }
        this.dueDate = dueDate;
    }

    public void changeValue(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PayableException("Value is required.");
        }
        this.value = value;
    }

    public void changeDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new PayableException("Description is required.");
        }
        this.description = description;
    }
}
