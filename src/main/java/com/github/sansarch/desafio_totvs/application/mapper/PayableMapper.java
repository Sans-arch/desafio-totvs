package com.github.sansarch.desafio_totvs.application.mapper;

import com.github.sansarch.desafio_totvs.domain.entity.Payable;
import com.github.sansarch.desafio_totvs.domain.factory.PayableFactory;
import com.github.sansarch.desafio_totvs.infrastructure.persistence.model.PayableModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PayableMapper {

    PayableMapper INSTANCE = Mappers.getMapper(PayableMapper.class);

    PayableModel toPayableModel(Payable payable);

    default Payable toPayableEntity(PayableModel payableModel) {
        return PayableFactory.createPayableWithExistingId(
                payableModel.getId(),
                payableModel.getDueDate(),
                payableModel.getPaymentDate(),
                payableModel.getValue(),
                payableModel.getDescription(),
                payableModel.getStatus()
        );
    }
}
