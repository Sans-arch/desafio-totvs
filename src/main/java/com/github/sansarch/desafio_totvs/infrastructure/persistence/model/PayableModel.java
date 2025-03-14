package com.github.sansarch.desafio_totvs.infrastructure.persistence.model;

import com.github.sansarch.desafio_totvs.domain.entity.PayableStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payables")
public class PayableModel {

    @Id
    private UUID id;

    @Column
    private LocalDateTime dueDate;

    @Column
    private LocalDateTime paymentDate;

    @Column
    private BigDecimal value;

    @Column(length = 50)
    private String description;

    @Enumerated(EnumType.STRING)
    private PayableStatus status;
}
