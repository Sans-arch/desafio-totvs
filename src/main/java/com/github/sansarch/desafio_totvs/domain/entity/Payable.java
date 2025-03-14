package com.github.sansarch.desafio_totvs.domain.entity;

import com.github.sansarch.desafio_totvs.domain.exception.payable.PayableAlreadyPaidException;
import com.github.sansarch.desafio_totvs.domain.exception.payable.PayableDescriptionRequiredException;
import com.github.sansarch.desafio_totvs.domain.exception.payable.PayableDueDateRequiredException;
import com.github.sansarch.desafio_totvs.domain.exception.payable.PayableValueRequiredException;
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
            throw new PayableDueDateRequiredException();
        }
        if (this.value == null) {
            throw new PayableValueRequiredException();
        }
        if (this.description == null || this.description.isBlank()) {
            throw new PayableDescriptionRequiredException();
        }
    }

    public void markAsPaid() {
        if (this.status != PayableStatus.PENDING) {
            throw new PayableAlreadyPaidException("The payable was already paid or canceled.");
        }
        this.paymentDate = LocalDateTime.now();
        this.status = PayableStatus.PAID;
    }

    public void cancel() {
        if (this.status == PayableStatus.PAID || this.status == PayableStatus.CANCELED) {
            throw new PayableAlreadyPaidException("The payable was already paid or canceled.");
        }
        this.status = PayableStatus.CANCELED;
    }

    public boolean isOverdue() {
        return LocalDateTime.now().isAfter(dueDate) && status == PayableStatus.PENDING;
    }

    public void changeDueDate(LocalDateTime dueDate) {
        if (dueDate == null) {
            throw new PayableDueDateRequiredException();
        }
        this.dueDate = dueDate;
    }

    public void changeValue(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new PayableValueRequiredException();
        }
        this.value = value;
    }

    public void changeDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new PayableDescriptionRequiredException();
        }
        this.description = description;
    }
}
