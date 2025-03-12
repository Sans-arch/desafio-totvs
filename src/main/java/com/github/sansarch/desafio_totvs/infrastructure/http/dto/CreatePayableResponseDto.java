package com.github.sansarch.desafio_totvs.infrastructure.http.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.sansarch.desafio_totvs.domain.entity.PayableStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreatePayableResponseDto(
        UUID id,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime dueDate,
        BigDecimal value,
        String description,
        PayableStatus status) {
}
