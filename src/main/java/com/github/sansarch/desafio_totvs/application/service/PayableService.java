package com.github.sansarch.desafio_totvs.application.service;

import com.github.sansarch.desafio_totvs.application.dto.CreatePayableDto;
import com.github.sansarch.desafio_totvs.application.mapper.PayableMapper;
import com.github.sansarch.desafio_totvs.domain.entity.Payable;
import com.github.sansarch.desafio_totvs.infrastructure.persistence.PayableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayableService {

    private final PayableRepository payableRepository;
    private PayableMapper mapper = PayableMapper.INSTANCE;

    public void createPayable(CreatePayableDto dto) {
        Payable payable = new Payable(
                dto.dueDate(),
                dto.value(),
                dto.description());

        var payableModel = mapper.toPayableModel(payable);
        payableRepository.save(payableModel);
    }
}
