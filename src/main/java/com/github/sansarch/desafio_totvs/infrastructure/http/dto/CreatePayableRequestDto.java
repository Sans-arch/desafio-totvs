package com.github.sansarch.desafio_totvs.infrastructure.http.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreatePayableRequestDto(
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime dueDate,
        BigDecimal value,
        String description) {
}
