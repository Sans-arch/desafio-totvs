package com.github.sansarch.desafio_totvs.application.service;

import com.github.sansarch.desafio_totvs.application.dto.CreatePayableInputDto;
import com.github.sansarch.desafio_totvs.application.dto.UpdatePayableInputDto;
import com.github.sansarch.desafio_totvs.application.mapper.PayableMapper;
import com.github.sansarch.desafio_totvs.domain.entity.Payable;
import com.github.sansarch.desafio_totvs.domain.entity.PayableStatus;
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

    public void payPayable(UUID id) {
        var payable = getPayable(id);
        payable.markAsPaid();
        payableRepository.save(PayableMapper.INSTANCE.toPayableModel(payable));
    }

    public Payable changePayableContent(UUID id, UpdatePayableInputDto dto) {
        var payable = getPayable(id);

        if (payable.getStatus() == PayableStatus.PAID) {
            throw new PayableException("The payable was already paid, cannot change its content.");
        }

        if (dto.dueDate() != null) {
            payable.changeDueDate(dto.dueDate());
        }
        if (dto.value() != null) {
            payable.changeValue(dto.value());
        }
        if (dto.description() != null) {
            payable.changeDescription(dto.description());
        }

        payableRepository.save(PayableMapper.INSTANCE.toPayableModel(payable));
        return payable;
    }
}
