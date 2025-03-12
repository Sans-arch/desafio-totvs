package com.github.sansarch.desafio_totvs.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TotalPaidOutputDto(
        LocalDateTime startDate,
        LocalDateTime endDate,
        BigDecimal totalPaid) {
}
