package com.github.sansarch.desafio_totvs.application.service;

import com.github.sansarch.desafio_totvs.application.dto.CreatePayableInputDto;
import com.github.sansarch.desafio_totvs.application.dto.CreatePayableOutputDto;
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

    public CreatePayableOutputDto createPayable(CreatePayableInputDto dto) {
        Payable payable = PayableFactory.createNewPayable(dto.dueDate(), dto.value(), dto.description());

        var payableModel = mapper.toPayableModel(payable);
        payableRepository.save(payableModel);

        return new CreatePayableOutputDto(
                payableModel.getId(),
                payableModel.getDueDate(),
                payableModel.getValue(),
                payableModel.getDescription(),
                payableModel.getStatus()
        );
    }

    public Payable getPayable(UUID id) {
        var payableModel = payableRepository.findById(id).orElseThrow(() -> new PayableException("Payable not found"));
        return PayableMapper.INSTANCE.toPayableEntity(payableModel);
    }
}
