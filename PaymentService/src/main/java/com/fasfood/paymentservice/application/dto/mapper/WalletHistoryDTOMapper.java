package com.fasfood.paymentservice.application.dto.mapper;

import com.fasfood.common.mapper.DTOMapper;
import com.fasfood.paymentservice.application.dto.response.WalletHistoryDTO;
import com.fasfood.paymentservice.domain.WalletHistory;
import com.fasfood.paymentservice.infrastructure.persistence.entity.WalletHistoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletHistoryDTOMapper extends DTOMapper<WalletHistoryDTO, WalletHistory, WalletHistoryEntity> {
}
