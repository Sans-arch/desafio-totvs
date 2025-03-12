package com.github.sansarch.desafio_totvs.infrastructure.persistence;

import com.github.sansarch.desafio_totvs.infrastructure.persistence.model.PayableModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PayableRepository extends JpaRepository<PayableModel, UUID> {
}
