package com.github.sansarch.desafio_totvs.infrastructure.repository;

import com.github.sansarch.desafio_totvs.infrastructure.repository.model.PayableModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayableRepository extends JpaRepository<PayableModel, Long> {
}
