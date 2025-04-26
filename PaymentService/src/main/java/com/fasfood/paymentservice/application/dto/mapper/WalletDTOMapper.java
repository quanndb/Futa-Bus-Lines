package com.fasfood.paymentservice.application.dto.mapper;

import com.fasfood.common.mapper.DTOMapper;
import com.fasfood.paymentservice.application.dto.response.WalletDTO;
import com.fasfood.paymentservice.domain.Wallet;
import com.fasfood.paymentservice.infrastructure.persistence.entity.WalletEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletDTOMapper extends DTOMapper<WalletDTO, Wallet, WalletEntity> {
}
