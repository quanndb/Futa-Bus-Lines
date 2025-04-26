package com.fasfood.paymentservice.application.dto.mapper;

import com.fasfood.common.mapper.DTOMapper;
import com.fasfood.paymentservice.application.dto.response.TransactionDTO;
import com.fasfood.paymentservice.domain.Transaction;
import com.fasfood.paymentservice.infrastructure.persistence.entity.TransactionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionDTOMapper extends DTOMapper<TransactionDTO, Transaction, TransactionEntity> {
}
