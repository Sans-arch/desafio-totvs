package com.github.sansarch.desafio_totvs.application.service;

import com.github.sansarch.desafio_totvs.application.dto.CreatePayableInputDto;
import com.github.sansarch.desafio_totvs.application.dto.TotalPaidOutputDto;
import com.github.sansarch.desafio_totvs.application.dto.UpdatePayableInputDto;
import com.github.sansarch.desafio_totvs.application.mapper.PayableMapper;
import com.github.sansarch.desafio_totvs.domain.entity.Payable;
import com.github.sansarch.desafio_totvs.domain.entity.PayableStatus;
import com.github.sansarch.desafio_totvs.domain.exception.payable.PayableAlreadyPaidException;
import com.github.sansarch.desafio_totvs.domain.exception.payable.PayableNotFoundException;
import com.github.sansarch.desafio_totvs.domain.factory.PayableFactory;
import com.github.sansarch.desafio_totvs.infrastructure.persistence.PayableRepository;
import com.github.sansarch.desafio_totvs.infrastructure.persistence.filter.PayableSpecification;
import com.github.sansarch.desafio_totvs.infrastructure.persistence.model.PayableModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    public Page<Payable> findPayables(String description, LocalDateTime dueDate, Pageable pageable) {
        Specification<PayableModel> filtersSpecification = Specification
                .where(PayableSpecification.hasDueDate(dueDate))
                .and(PayableSpecification.hasDescription(description));

        return payableRepository
                .findAll(filtersSpecification, pageable)
                .map(payableModel -> PayableFactory.createPayableWithExistingId(
                        payableModel.getId(),
                        payableModel.getDueDate(),
                        payableModel.getPaymentDate(),
                        payableModel.getValue(),
                        payableModel.getDescription(),
                        payableModel.getStatus()
                ));
    }

    public Payable getPayable(UUID id) {
        var payableModel = payableRepository.findById(id).orElseThrow(() -> new PayableNotFoundException("Payable not found"));
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
            throw new PayableAlreadyPaidException("The payable was already paid, cannot change its content.");
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

    public TotalPaidOutputDto calculateTotalPaid(LocalDateTime startDate, LocalDateTime endDate) {
        Specification<PayableModel> spec = Specification
                .where(PayableSpecification.hasDueDateBetween(startDate, endDate))
                .and(PayableSpecification.hasStatusPaid());

        List<PayableModel> payables = payableRepository.findAll(spec);
        BigDecimal totalPaid = payables.stream()
                .map(PayableModel::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new TotalPaidOutputDto(startDate, endDate, totalPaid);
    }

    public void processCsv(MultipartFile file) throws IOException {
        List<Payable> payables = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] data = line.split(",");

                String description = data[0].trim();
                BigDecimal value = new BigDecimal(data[1].trim());
                LocalDateTime dueDate = LocalDateTime.parse(data[2].trim());

                Payable payable = PayableFactory.createNewPayable(dueDate, value, description);
                var payableModel = PayableMapper.INSTANCE.toPayableModel(payable);
                payableRepository.save(payableModel);
                payables.add(payable);
            }
        }
    }
}
