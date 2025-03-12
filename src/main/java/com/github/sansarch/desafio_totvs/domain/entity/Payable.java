package com.github.sansarch.desafio_totvs.domain.entity;

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

    public void cancel() {
        this.status = PayableStatus.CANCELED;
    }
}
