package com.github.sansarch.desafio_totvs.application.service;

import com.github.sansarch.desafio_totvs.application.dto.CreatePayableInputDto;
import com.github.sansarch.desafio_totvs.application.mapper.PayableMapper;
import com.github.sansarch.desafio_totvs.domain.entity.Payable;
import com.github.sansarch.desafio_totvs.domain.exception.PayableException;
import com.github.sansarch.desafio_totvs.domain.factory.PayableFactory;
import com.github.sansarch.desafio_totvs.infrastructure.persistence.PayableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PayableService {

    private final PayableRepository payableRepository;
    private PayableMapper mapper = PayableMapper.INSTANCE;

    public Payable createPayable(CreatePayableInputDto dto) {
        Payable payable = PayableFactory.createNewPayable(dto.dueDate(), dto.value(), dto.description());

        var payableModel = mapper.toPayableModel(payable);
        payableRepository.save(payableModel);

        return PayableMapper.INSTANCE.toPayableEntity(payableModel);
    }

    public Payable getPayable(UUID id) {
        var payableModel = payableRepository.findById(id).orElseThrow(() -> new PayableException("Payable not found"));
        return PayableMapper.INSTANCE.toPayableEntity(payableModel);
    }

    public void cancelPayable(UUID id) {
        var payable = getPayable(id);
        payable.cancel();
        payableRepository.save(PayableMapper.INSTANCE.toPayableModel(payable));
    }
}
