package com.fasfood.paymentservice.infrastructure.persistence.mapper;

import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.paymentservice.domain.Transaction;
import com.fasfood.paymentservice.infrastructure.persistence.entity.TransactionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionEntityMapper extends EntityMapper<Transaction, TransactionEntity> {
}
