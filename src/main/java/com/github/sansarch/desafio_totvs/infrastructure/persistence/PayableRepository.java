package com.github.sansarch.desafio_totvs.infrastructure.persistence;

import com.github.sansarch.desafio_totvs.infrastructure.persistence.model.PayableModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayableRepository extends JpaRepository<PayableModel, Long> {
}
