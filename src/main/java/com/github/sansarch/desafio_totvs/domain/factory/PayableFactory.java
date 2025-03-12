package com.github.sansarch.desafio_totvs.domain.factory;

import com.github.sansarch.desafio_totvs.domain.entity.Payable;
import com.github.sansarch.desafio_totvs.domain.entity.PayableStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class PayableFactory {

    public static Payable createNewPayable(LocalDateTime dueDate, BigDecimal value, String description) {
        return new Payable(UUID.randomUUID(), dueDate, null, value, description, PayableStatus.PENDING);
    }

    public static Payable createPayableWithExistingId(UUID id, LocalDateTime dueDate, LocalDateTime paymentDate,
                                                      BigDecimal value, String description, PayableStatus status) {
        return new Payable(id, dueDate, paymentDate, value, description, status);
    }
}
