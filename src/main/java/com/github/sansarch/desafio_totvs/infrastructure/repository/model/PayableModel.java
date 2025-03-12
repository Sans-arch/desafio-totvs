package com.github.sansarch.desafio_totvs.infrastructure.repository.model;

import com.github.sansarch.desafio_totvs.domain.entity.PayableStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payables")
public class PayableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime dueDate;

    @Column
    private LocalDateTime paymentDate;

    @Column
    private BigDecimal value;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    private PayableStatus status;
}
