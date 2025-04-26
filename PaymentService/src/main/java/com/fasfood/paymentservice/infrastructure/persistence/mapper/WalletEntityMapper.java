package com.fasfood.paymentservice.infrastructure.persistence.mapper;

import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.paymentservice.domain.Wallet;
import com.fasfood.paymentservice.infrastructure.persistence.entity.WalletEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletEntityMapper extends EntityMapper<Wallet, WalletEntity> {
}
