package com.fasfood.paymentservice.application.dto.mapper;

import com.fasfood.common.mapper.DTOMapper;
import com.fasfood.paymentservice.application.dto.response.WalletCommandDTO;
import com.fasfood.paymentservice.domain.WalletCommand;
import com.fasfood.paymentservice.infrastructure.persistence.entity.WalletCommandEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletCommandDTOMapper extends DTOMapper<WalletCommandDTO, WalletCommand, WalletCommandEntity> {
}
