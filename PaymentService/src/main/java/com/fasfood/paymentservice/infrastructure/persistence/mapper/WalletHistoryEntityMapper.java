package com.fasfood.paymentservice.infrastructure.persistence.mapper;

import com.fasfood.common.mapper.EntityMapper;
import com.fasfood.paymentservice.domain.WalletHistory;
import com.fasfood.paymentservice.infrastructure.persistence.entity.WalletHistoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletHistoryEntityMapper extends EntityMapper<WalletHistory, WalletHistoryEntity> {
}
