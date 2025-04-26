package com.fasfood.paymentservice.infrastructure.persistence.mapper;

import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.paymentservice.domain.WalletCommand;
import com.fasfood.paymentservice.infrastructure.persistence.entity.WalletCommandEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletCommandEntityMapper extends EntityMapper<WalletCommand, WalletCommandEntity> {
}
