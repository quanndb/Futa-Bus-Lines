package com.fasfood.persistence.custom;

import com.fasfood.common.dto.response.StatisticResponse;
import com.fasfood.common.query.PagingQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntityRepository<E, I> extends JpaRepository<E, I>,
        CustomEntityRepository<E, PagingQuery, StatisticResponse> {
}
