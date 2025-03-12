package com.github.sansarch.desafio_totvs.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreatePayableDto(
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime dueDate,
        BigDecimal value,
        String description) {
}
